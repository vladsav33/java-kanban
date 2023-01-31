package task;

import manager.FileBackedTasksManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    final String configFile = "current.cfg";
    private TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager(new File(configFile));
    }

    @Test
    public void epicEmpty() {
        manager.createEpic("Ремонт", "Обновить ремонт");

        String result = manager.getEpic(1).toString();
        assertTrue (result.equals("1,EPIC,NEW,Ремонт,Обновить ремонт,null,null"));
    }


    @Test
    public void epicNew() {
        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.createSubtask("Покупки", "Купить краску", 1);
        manager.createSubtask("Покупки", "Купить кисточку", 1);

        String result = manager.getEpic(1).toString();
        assertTrue (result.equals("1,EPIC,NEW,Ремонт,Обновить ремонт,null,null"));
    }


    @Test
    public void epicDone() {
        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.createSubtask("Покупки", "Купить краску", 1);
        manager.createSubtask("Покупки", "Купить кисточку", 1);
        SubTask subTask = new SubTask(2, Type.SUBTASK, Status.DONE, "Покупки", "Купить краску", 1);
        manager.updateSubtask(subTask);
        subTask = new SubTask(3, Type.SUBTASK, Status.DONE, "Покупки", "Купить кисточку", 1);
        manager.updateSubtask(subTask);

        String result = manager.getEpic(1).toString();
        System.out.println(result);
        assertTrue (result.equals("1,EPIC,DONE,Ремонт,Обновить ремонт,null,null"));
    }


    @Test
    public void epicNewAndDone() {
        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.createSubtask("Покупки", "Купить краску", 1);
        manager.createSubtask("Покупки", "Купить кисточку", 1);
        SubTask subTask = new SubTask(2, Type.SUBTASK, Status.DONE, "Покупки", "Купить краску", 1);
        manager.updateSubtask(subTask);

        String result = manager.getEpic(1).toString();
        assertTrue (result.equals("1,EPIC,IN_PROGRESS,Ремонт,Обновить ремонт,null,null"));
    }

    @Test
    public void epicInProgress() {
        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.createSubtask("Покупки", "Купить краску", 1);
        manager.createSubtask("Покупки", "Купить кисточку", 1);
        SubTask subTask = new SubTask(2, Type.SUBTASK, Status.IN_PROGRESS, "Покупки", "Купить краску", 1);
        manager.updateSubtask(subTask);
        subTask = new SubTask(3, Type.SUBTASK, Status.IN_PROGRESS, "Покупки", "Купить кисточку", 1);
        manager.updateSubtask(subTask);

        String result = manager.getEpic(1).toString();
        assertTrue (result.equals("1,EPIC,IN_PROGRESS,Ремонт,Обновить ремонт,null,null"));
    }

    @Test
    public void epicTime() {
        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.createSubtask("Покупки", "Купить краску", 1);
        manager.createSubtask("Покупки", "Купить кисточку", 1);
        SubTask subTask = new SubTask(2, Type.SUBTASK, Status.DONE, "Покупки", "Купить краску", 1,
                LocalTime.of(17, 00), Duration.ofMinutes(20));
        manager.updateSubtask(subTask);
        subTask = new SubTask(3, Type.SUBTASK, Status.DONE, "Покупки", "Купить кисточку", 1,
                LocalTime.of(17, 20), Duration.ofMinutes(20));
        manager.updateSubtask(subTask);

        String result = manager.getEpic(1).toString();
        System.out.println(result);
        assertTrue (result.equals("1,EPIC,DONE,Ремонт,Обновить ремонт,17:00,17:40"));
    }
}