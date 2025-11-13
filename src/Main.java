import dz_1.MyHashMap;

public class Main {
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put("apple", 10);
        map.put("banana", 20);
        map.put("orange", 30);

        System.out.println(map.get("banana"));
        System.out.println(map.get("grape"));

        map.remove("banana");
        System.out.println(map.get("banana"));

        map.put("apple", 99);
        System.out.println(map.get("apple"));

        System.out.println("Размер хранилища: " + map.size());
    }
}
