package io.pivotal.alex.pal.paltracker.tracker;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private Map<Long, TimeEntry> timeEntries;

    public InMemoryTimeEntryRepository() {
        this.timeEntries = new HashMap<>();
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry createdTimeEntry = new TimeEntry(this.timeEntries.size() + 1, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        timeEntries.put((long) (this.timeEntries.size() + 1), createdTimeEntry);
        return createdTimeEntry;
    }

    @Override
    public TimeEntry find(long id) {
        return this.timeEntries.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return this.timeEntries == null ? Collections.emptyList() : new ArrayList(this.timeEntries.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry updatedTimeEntry = new TimeEntry(id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        this.timeEntries.put(id, updatedTimeEntry);
        return updatedTimeEntry;
    }

    @Override
    public TimeEntry delete(long id) {
        TimeEntry deleted = this.timeEntries.get(id);
        this.timeEntries.remove(id);
        return deleted;
    }
}
