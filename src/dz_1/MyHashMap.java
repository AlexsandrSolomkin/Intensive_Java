package dz_1;
import java.util.*;

public class MyHashMap<K, V> {

    // Начальный размер массива бакетов
    private static final int DEFAULT_CAPACITY = 16;

    // Коэффициент загрузки (при 75% заполнения — увеличиваем размер)
    private static final float LOAD_FACTOR = 0.75f;

    // Массив "бакетов", каждый из которых хранит цепочку Node<K, V>
    private Node<K, V>[] table;

    // Количество элементов в карте
    private int size = 0;

    /**
     * Внутренний класс — элемент (узел) хэш-таблицы.
     * Хранит пару "ключ-значение" и ссылку на следующий узел (для разрешения коллизий).
     */
    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next; // ссылка на следующий элемент в цепочке

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Конструктор: создаём массив бакетов заданного размера
    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    /**
     * Хэш-функция:
     * вычисляет индекс бакета по ключу (с учётом размера массива).
     */
    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    /**
     * Добавление или обновление элемента.
     * Если ключ уже есть — обновляем значение.
     * Если нет — вставляем новый узел.
     */
    public void put(K key, V value) {
        // Проверяем, не пора ли увеличить таблицу
        if ((float) size / table.length >= LOAD_FACTOR) {
            resize();
        }

        int index = hash(key); // находим индекс бакета
        Node<K, V> current = table[index];

        // Если бакет пуст — просто вставляем новый элемент
        if (current == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }

        // Если бакет не пуст, ищем, нет ли уже такого ключа
        Node<K, V> prev = null;
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                // Если ключ найден — обновляем значение
                current.value = value;
                return;
            }
            prev = current;
            current = current.next;
        }

        // Если дошли до конца цепочки — добавляем новый узел в конец
        prev.next = new Node<>(key, value);
        size++;
    }

    /**
     * Получение значения по ключу.
     * Возвращает значение, если ключ найден, иначе — null.
     */
    public V get(K key) {
        int index = hash(key);
        Node<K, V> current = table[index];

        // Ищем элемент с нужным ключом
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }

        // Если не нашли — возвращаем null
        return null;
    }

    /**
     * Удаление элемента по ключу.
     * Если элемент найден — удаляется из цепочки.
     */
    public void remove(K key) {
        int index = hash(key);
        Node<K, V> current = table[index];
        Node<K, V> prev = null;

        // Ищем элемент в цепочке
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                // Если удаляем первый элемент в цепочке
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    // Пропускаем удаляемый элемент
                    prev.next = current.next;
                }
                size--;
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    /**
     * Увеличивает размер массива в 2 раза и перераспределяет все элементы.
     * Это нужно, чтобы уменьшить количество коллизий при увеличении числа элементов.
     */
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0; // пересчитаем при повторной вставке

        // Перехэшируем все элементы
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    // Возвращает текущее количество элементов
    public int size() {
        return size;
    }
}
