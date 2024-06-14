package org.example.service;

import com.opencsv.CSVReader;
import org.example.model.Status;
import org.example.model.Task;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class DataLoaderTest {
    @Spy
    @InjectMocks
    private DataLoader dataLoader;
    @Mock
    private TaskService taskService;
    @Mock
    private UserService userService;
    @Mock
    private CSVReader csvReader;
    @Mock
    private FileWriter fileWriter;


    @Test
    public void testDownloadFromFileUser() throws Exception {
        doReturn(csvReader).when(dataLoader).createCSVReader(any(FileReader.class));
        when(csvReader.readNext()).thenReturn(new String[]
                {"1", "Джек"},(String[]) null);

        dataLoader.downloadFromFile(0);

        verify(userService, times(1)).returnUserList();
        verify(csvReader, times(2)).readNext();
    }
    @Test
    public void testDownloadFromFileTask() throws Exception {
        doReturn(csvReader).when(dataLoader).createCSVReader(any(FileReader.class));
        when(csvReader.readNext()).thenReturn(new String[]
                {"1", "Задача 1", "Текст 1", "1", "01.01.2024", "Новое"},(String[]) null);
        when(taskService.addTask(any(Task.class))).thenReturn(true);

        dataLoader.downloadFromFile(1);

        verify(taskService, times(1)).addTask(any(Task.class));
        verify(csvReader, times(2)).readNext();
    }
    @Test
    public void testSaveToFileUser() throws Exception {
        doReturn(fileWriter).when(dataLoader).createFileWriter(anyString());
        List<User> userList = new ArrayList<>(List.of(new User(1, "Джек"), new User(2, "Джон")));
        when(userService.returnUserList()).thenReturn((ArrayList<User>) userList);

        dataLoader.saveToFile(0);

        verify(fileWriter, times(1)).write("1,Джек\n");
        verify(fileWriter, times(1)).write("2,Джон\n");
    }
}
