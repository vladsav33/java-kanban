package manager;

import task.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save() {
        try {
            Files.writeString(file.toPath(), "id,type,name,status,description,epic\n",
                    StandardOpenOption.TRUNCATE_EXISTING);
            for (Task task : tasks.values()) {
                Files.writeString(file.toPath(), task.toString() + "\n", StandardOpenOption.APPEND);
            }
            for (Epic epic : epics.values()) {
                Files.writeString(file.toPath(), epic.toString() + "\n", StandardOpenOption.APPEND);
            }
            for (SubTask subTask : subTasks.values()) {
                Files.writeString(file.toPath(), subTask.toString() + "\n", StandardOpenOption.APPEND);
            }
            Files.writeString(file.toPath(), "\n" + historyToString(historyManager), StandardOpenOption.APPEND);
        } catch (IOException ioException) {
            throw new ManagerSaveException();
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubtask(int id) {
        SubTask subTask = super.getSubtask(id);
        save();
        return subTask;
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public Task createTask(String name, String description) {
        Task task = super.createTask(name, description);
        save();
        return task;
    }

    @Override
    public Epic createEpic(String name, String description) {
        Epic epic = super.createEpic(name, description);
        save();
        return epic;
    }

    @Override
    public SubTask createSubtask(String name, String description, int idEpic) {
        SubTask subTask = super.createSubtask(name, description, idEpic);
        save();
        return subTask;
    }

    @Override
    public Task updateTask(Task task) {
        Task taskResult = super.updateTask(task);
        save();
        return taskResult;
    }

    @Override
    public Task updateEpic(Epic epic) {
        Task task = super.updateEpic(epic);
        save();
        return task;
    }

    @Override
    public Task updateSubtask(SubTask subTask) {
        Task task = super.updateSubtask(subTask);
        save();
        return task;
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder result = new StringBuilder();

        for (Task item : manager.getHistory()) {
            result.append(item.getId() + ",");
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    public static List<Integer> historyFromString(String value) {
        String[] elements = value.split(",");
        List<Integer> list = new ArrayList<>();
        InMemoryTaskManager.historyManager = new InMemoryHistoryManager();

        for (String item : elements) {
            if (InMemoryTaskManager.tasks.containsKey(Integer.parseInt(item))) {
                list.add(Integer.parseInt(item));
                historyManager.add(InMemoryTaskManager.tasks.get(Integer.parseInt(item)));
            } else if (InMemoryTaskManager.epics.containsKey(Integer.parseInt(item))) {
                list.add(Integer.parseInt(item));
                historyManager.add(InMemoryTaskManager.epics.get(Integer.parseInt(item)));
            } else if (InMemoryTaskManager.subTasks.containsKey(Integer.parseInt(item))) {
                list.add(Integer.parseInt(item));
                historyManager.add(InMemoryTaskManager.subTasks.get(Integer.parseInt(item)));
            }
        }
        return list;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        String fileContent;
        try {
            fileContent = Files.readString(file.toPath());
            String[] line = fileContent.split("\n");
            for (int lineNo = 1; lineNo < line.length - 2; lineNo++) {
                String[] elements = line[lineNo].split(",");
                switch (Type.valueOf(elements[1])) {
                    case TASK:
                        Task task = new Task(Integer.parseInt(elements[0]), Type.valueOf(elements[1]), Status.valueOf(elements[2]),
                                elements[3], elements[4]);
                        tasks.put(task.getId(), task);
                        break;
                    case EPIC:
                        Epic epic = new Epic(Integer.parseInt(elements[0]), Type.valueOf(elements[1]), Status.valueOf(elements[2]),
                                elements[3], elements[4]);
                        epics.put(epic.getId(), epic);
                        break;
                    case SUBTASK:
                        SubTask subTask = new SubTask(Integer.parseInt(elements[0]), Type.valueOf(elements[1]), Status.valueOf(elements[2]),
                                elements[3], elements[4], Integer.parseInt(elements[5]));
                        subTasks.put(subTask.getId(), subTask);
                        break;
                }
            }
            historyFromString(line[line.length - 1]);
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return manager;
    }
}
