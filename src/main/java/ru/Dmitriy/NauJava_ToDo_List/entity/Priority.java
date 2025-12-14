package ru.Dmitriy.NauJava_ToDo_List.entity;

public enum Priority {
    LOW("Низкий"),
    MEDIUM("Средний"),
    HIGH("Высокий");

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Priority fromString(String text) {
        for (Priority priority : Priority.values()) {
            if (priority.name().equalsIgnoreCase(text) ||
                    priority.displayName.equalsIgnoreCase(text)) {
                return priority;
            }
        }
        return MEDIUM;
    }

    public int getWeight() {
        switch (this) {
            case LOW: return 1;
            case MEDIUM: return 2;
            case HIGH: return 3;
            default: return 2;
        }
    }
}
