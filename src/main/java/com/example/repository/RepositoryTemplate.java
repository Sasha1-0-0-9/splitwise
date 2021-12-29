package com.example.repository;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RepositoryTemplate {

    private final static String FILE_PATH = "src/main/resources/";

    public synchronized static <T> Map<Integer, T> getItems(String fileName) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH + fileName))) {
            return (Map<Integer, T>) objectInputStream.readObject();
        } catch (EOFException e) {
            return new ConcurrentHashMap<>();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> void setItems(Map<Integer, T> itemsCollection, String fileName) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_PATH + fileName))) {
            objectOutputStream.writeObject(itemsCollection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static <T> void save(Integer id, T t, String fileName) {
        Map<Integer, T> items = getItems(fileName);

        items.put(id, t);

        setItems(items, fileName);
    }

    public synchronized static <T> void deleteAll(Set<Integer> ids, String fileName) {
        Map<Integer, T> items = getItems(fileName);

        ids.forEach(items::remove);

        setItems(items, fileName);
    }

    public static int getSize(String fileName) {
        return getItems(fileName).size();
    }
}