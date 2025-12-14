package ru.Dmitriy.NauJava_ToDo_List.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticsDTO {

    private Long userId;
    private String userFullName;

    private Long totalTasks;
    private Long completedTasks;
    private Long activeTasks;

    private Long highPriorityTasks;
    private Long mediumPriorityTasks;
    private Long lowPriorityTasks;

    private Double completionPercentage;
    private Long averageTasksPerDay;

    public StatisticsDTO() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public Long getTotalTasks() { return totalTasks; }
    public void setTotalTasks(Long totalTasks) { this.totalTasks = totalTasks; }

    public Long getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(Long completedTasks) { this.completedTasks = completedTasks; }

    public Long getActiveTasks() { return activeTasks; }
    public void setActiveTasks(Long activeTasks) { this.activeTasks = activeTasks; }

    public Long getHighPriorityTasks() { return highPriorityTasks; }
    public void setHighPriorityTasks(Long highPriorityTasks) { this.highPriorityTasks = highPriorityTasks; }

    public Long getMediumPriorityTasks() { return mediumPriorityTasks; }
    public void setMediumPriorityTasks(Long mediumPriorityTasks) { this.mediumPriorityTasks = mediumPriorityTasks; }

    public Long getLowPriorityTasks() { return lowPriorityTasks; }
    public void setLowPriorityTasks(Long lowPriorityTasks) { this.lowPriorityTasks = lowPriorityTasks; }

    public Double getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(Double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public Long getAverageTasksPerDay() { return averageTasksPerDay; }
    public void setAverageTasksPerDay(Long averageTasksPerDay) { this.averageTasksPerDay = averageTasksPerDay; }

    public void calculateCompletionPercentage() {
        if (totalTasks != null && totalTasks > 0) {
            this.completionPercentage = (completedTasks.doubleValue() / totalTasks.doubleValue()) * 100;
        } else {
            this.completionPercentage = 0.0;
        }
    }
}
