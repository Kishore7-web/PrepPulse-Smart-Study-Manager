package com.preppulse.config;
import com.preppulse.model.StudyTask; import com.preppulse.model.StudyTopic; import com.preppulse.repository.StudyTaskRepository; import com.preppulse.repository.StudyTopicRepository;
import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import java.time.LocalDate;
@Configuration public class DataSeeder {
 @Bean CommandLineRunner seedSampleData(StudyTaskRepository tasks,StudyTopicRepository topics){return args->{
  if(tasks.count()==0){StudyTask a=new StudyTask();a.setTitle("Revise Java Collections");a.setSubject("Java Programming");a.setPriority(StudyTask.Priority.HIGH);a.setDueDate(LocalDate.now().plusDays(1));a.setEstimatedMinutes(45);a.setNotes("Review ArrayList, HashMap, and iterators.");tasks.save(a);
   StudyTask b=new StudyTask();b.setTitle("Practice time complexity");b.setSubject("Data Structures");b.setPriority(StudyTask.Priority.MEDIUM);b.setDueDate(LocalDate.now().plusDays(2));b.setEstimatedMinutes(30);b.setNotes("Solve a few MCQs and coding problems.");tasks.save(b);}
  if(topics.count()==0){StudyTopic a=new StudyTopic();a.setTitle("Java Collections Framework");a.setSubject("Java Programming");a.setDifficulty(StudyTopic.Difficulty.MEDIUM);a.setLastReviewedDate(LocalDate.now());a.setNextReviewDate(LocalDate.now().plusDays(1));topics.save(a);
   StudyTopic b=new StudyTopic();b.setTitle("Time Complexity Basics");b.setSubject("Data Structures");b.setDifficulty(StudyTopic.Difficulty.HARD);b.setLastReviewedDate(LocalDate.now());b.setNextReviewDate(LocalDate.now());topics.save(b);}
 };}
}
