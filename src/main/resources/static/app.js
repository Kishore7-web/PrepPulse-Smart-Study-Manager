(() => {
  const byId = (id) => document.getElementById(id);
  const dateNode = byId('today');
  if (dateNode) dateNode.textContent = new Date().toLocaleDateString(undefined, { weekday: 'short', month: 'short', day: 'numeric', year: 'numeric' });

  // Reminder interval checkboxes serialize to the task's comma-separated reminder setting.
  const offsets = [...document.querySelectorAll('.reminder-offset')];
  const offsetField = byId('reminderIntervals');
  function syncOffsets() { if (offsetField) offsetField.value = offsets.filter(x => x.checked).map(x => x.value).join(',') || '15'; }
  offsets.forEach(x => x.addEventListener('change', syncOffsets)); syncOffsets();

  // Native browser notifications are optional: the application remains useful without permission.
  function toast(title, message) {
    const host = byId('toastHost'); if (!host) return;
    const node = document.createElement('div'); node.className = 'toast';
    const strong = document.createElement('strong'); strong.textContent = title;
    const text = document.createElement('span'); text.textContent = message;
    node.append(strong, text); host.appendChild(node); setTimeout(() => node.remove(), 7000);
  }
  const enable = byId('enableNotifications');
  if (enable) enable.addEventListener('click', async () => {
    if (!('Notification' in window)) { toast('Browser alerts unavailable', 'Your browser does not support notifications.'); return; }
    const permission = await Notification.requestPermission();
    toast(permission === 'granted' ? 'Browser alerts enabled' : 'Permission not granted', permission === 'granted' ? 'PrepPulse can now show reminder alerts while this dashboard is open.' : 'You can still see reminder activity in PrepPulse.');
  });
  let lastSeenId = Number(document.body.dataset.latestReminder || 0);
  async function checkReminders() {
    try {
      const response = await fetch('/api/reminders/recent?after=' + lastSeenId, { headers: { 'Accept': 'application/json' } });
      if (!response.ok) return;
      const list = await response.json();
      for (const item of list) {
        if (!item.id || item.id <= lastSeenId) continue;
        lastSeenId = item.id;
        toast('PrepPulse reminder', item.message || item.taskTitle || 'A study task needs attention.');
        if ('Notification' in window && Notification.permission === 'granted') new Notification('PrepPulse reminder', { body: item.message || item.taskTitle });
      }
    } catch (e) { /* Offline dashboard: retry on the next poll. */ }
  }
  setInterval(checkReminders, 30000);

  // Smart Rescue Mode builds a plan on the server so the same rules can be tested independently.
  const rescueForm = byId('rescueForm'); const result = byId('rescueResult');
  async function buildRescue(minutes) {
    if (!result) return;
    result.replaceChildren(); const loading = document.createElement('p'); loading.className = 'muted'; loading.textContent = 'Building a realistic first step…'; result.appendChild(loading);
    try {
      const response = await fetch('/api/rescue-plan', { method: 'POST', headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' }, body: JSON.stringify({ availableMinutes: minutes }) });
      if (!response.ok) throw new Error('Could not build the plan. Refresh the page and try again.');
      const plan = await response.json(); result.replaceChildren();
      const summary = document.createElement('p'); summary.className = 'plan-summary'; summary.textContent = plan.message; result.appendChild(summary);
      const meta = document.createElement('div'); meta.className = 'plan-meta'; meta.textContent = `${plan.plannedMinutes} of ${plan.availableMinutes} min planned · ${plan.tasksCovered} task(s) included · ${plan.tasksStillNeedTime} task(s) still need time`; result.appendChild(meta);
      const list = document.createElement('div'); list.className = 'plan-blocks';
      for (const block of (plan.blocks || [])) {
        const row = document.createElement('div'); row.className = 'plan-block ' + String(block.type || '').toLowerCase();
        const duration = document.createElement('span'); duration.className = 'block-time'; duration.textContent = `${block.durationMinutes}m`;
        const copy = document.createElement('div'); const title = document.createElement('strong'); title.textContent = block.type === 'BREAK' ? '☕ ' + block.title : block.title;
        const subject = document.createElement('small'); subject.textContent = block.type === 'BREAK' ? block.note : `${block.subject} · ${block.note}`;
        copy.append(title, subject); row.append(duration, copy); list.appendChild(row);
      }
      result.appendChild(list);
      if (!plan.blocks || plan.blocks.length === 0) { const empty = document.createElement('p'); empty.className = 'muted'; empty.textContent = 'All clear — add a task when you are ready.'; result.appendChild(empty); }
    } catch (e) { result.replaceChildren(); const msg = document.createElement('p'); msg.className = 'field-error'; msg.textContent = e.message || 'Something went wrong.'; result.appendChild(msg); }
  }
  if (rescueForm) rescueForm.addEventListener('submit', e => { e.preventDefault(); buildRescue(Number(byId('availableMinutes').value || 60)); });
  const comeback = byId('comebackButton'); if (comeback) comeback.addEventListener('click', () => { byId('availableMinutes').value = 15; buildRescue(15); });

  // Focus timer: saves the completed or manually stopped session to SQL through the API.
  let interval = null, totalSeconds = 25 * 60, remainingSeconds = totalSeconds, startTime = null, accumulatedSeconds = 0, running = false;
  const display = byId('timerDisplay'), state = byId('timerState'), startBtn = byId('timerStart'), pauseBtn = byId('timerPause'), resetBtn = byId('timerReset');
  function showTime() { if (display) display.textContent = `${String(Math.floor(remainingSeconds / 60)).padStart(2,'0')}:${String(remainingSeconds % 60).padStart(2,'0')}`; }
  function chooseDuration() { const selected = byId('focusType')?.selectedOptions[0]; return Math.max(1, Number(selected?.dataset.minutes || 25)); }
  function clearTimer() { if (interval) clearInterval(interval); interval = null; running = false; if (startBtn) startBtn.disabled = false; if (pauseBtn) pauseBtn.disabled = true; }
  async function saveSession(minutes, type) {
    if (minutes < 1) { toast('Session not saved', 'Study sessions shorter than one full minute are not recorded.'); return; }
    const taskValue = byId('focusTask')?.value;
    try {
      const response = await fetch('/api/focus-sessions', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ durationMinutes: minutes, taskId: taskValue ? Number(taskValue) : null, sessionType: type }) });
      if (!response.ok) throw new Error('The session could not be saved.');
      toast(type === 'BREAK' ? 'Break saved' : 'Focus session saved', `${minutes} minute(s) recorded. Refresh the page to update insights.`);
    } catch (e) { toast('Could not save session', e.message || 'Check that PrepPulse is still running.'); }
  }
  function finishTimer(auto = false) { clearTimer(); const type = byId('focusType')?.value || 'FOCUS'; const elapsed = Math.floor(accumulatedSeconds / 60); if (state) state.textContent = auto ? 'SESSION COMPLETE' : 'SESSION STOPPED'; if (startBtn) startBtn.textContent = '▶ Start again'; saveSession(elapsed, type); if (auto) toast('Nice work! ✨', 'Your timer finished. Take a breath before the next block.'); }
  if (startBtn && display) {
    byId('focusType').addEventListener('change', () => { if (running) return; totalSeconds = chooseDuration() * 60; remainingSeconds = totalSeconds; accumulatedSeconds = 0; showTime(); if(state)state.textContent='READY WHEN YOU ARE'; });
    startBtn.addEventListener('click', () => { if (running) return; if (remainingSeconds <= 0 || accumulatedSeconds === 0) { totalSeconds = chooseDuration() * 60; remainingSeconds = totalSeconds; accumulatedSeconds = 0; } running = true; startTime = Date.now() - accumulatedSeconds * 1000; startBtn.disabled = true; pauseBtn.disabled = false; if(state)state.textContent='FOCUS IN PROGRESS'; interval = setInterval(() => { const elapsed = Math.floor((Date.now() - startTime) / 1000); const target = totalSeconds - elapsed; remainingSeconds = Math.max(0, target); accumulatedSeconds = elapsed; showTime(); if (remainingSeconds <= 0) finishTimer(true); }, 250); });
    pauseBtn.addEventListener('click', () => { if (!running) return; accumulatedSeconds = Math.floor((Date.now() - startTime) / 1000); remainingSeconds = Math.max(0, totalSeconds - accumulatedSeconds); clearTimer(); startBtn.disabled = false; if(state)state.textContent='PAUSED'; showTime(); });
    resetBtn.addEventListener('click', () => { clearTimer(); totalSeconds = chooseDuration() * 60; remainingSeconds = totalSeconds; accumulatedSeconds = 0; showTime(); startBtn.textContent = '▶ Start'; if(state)state.textContent='READY WHEN YOU ARE'; });
  }
})();
