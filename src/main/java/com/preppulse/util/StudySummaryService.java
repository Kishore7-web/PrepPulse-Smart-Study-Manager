package com.preppulse.util;

import com.preppulse.model.StudyTask;
import com.preppulse.repository.StudyTaskRepository;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class StudySummaryService {
    private final StudyTaskRepository repository;
    private final ExecutorService studyExecutor;

    public StudySummaryService(StudyTaskRepository repository, ExecutorService studyExecutor) {
        this.repository = repository;
        this.studyExecutor = studyExecutor;
    }

    // Demonstrates background work using Java's CompletableFuture and a managed thread pool.
    public CompletableFuture<String> generateSummaryAsync() {
        return CompletableFuture.supplyAsync(() -> {
            var tasks = repository.findAll();
            long pending = tasks.stream().filter(t -> !t.isCompleted()).count();
            String next = tasks.stream().filter(t -> !t.isCompleted())
                    .min((a, b) -> a.getDueDate().compareTo(b.getDueDate()))
                    .map(StudyTask::getTitle).orElse("No pending tasks");
            return "You have " + pending + " pending task(s). Next task: " + next + ".";
        }, studyExecutor);
    }
}
