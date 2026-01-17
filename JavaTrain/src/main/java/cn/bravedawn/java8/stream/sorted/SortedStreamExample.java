package cn.bravedawn.java8.stream.sorted;

import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author : depers
 * @program : JavaTrain
 * @date : Created in 2024/6/4 14:43
 *
 * stream sort的使用
 * @see <a href="https://www.baeldung.com/java-8-comparator-comparing">Guide to Java 8 Comparator.comparing()</a>
 */
public class SortedStreamExample {

    public static void main(String[] args) {
        Student s1 = new Student("冯晓", 27, new Date(96, 9, 21, 0, 0, 0));
        Student s2 = new Student("李明", 27, new Date(96, 9, 21, 1, 10, 59));
        Student s3 = new Student("张婷", 27, new Date(98, 9, 21, 0, 0, 0));

        Date time = new Date(2019, 7, 8, 0, 0, 0);

        List<Student> list = new ArrayList<>(Arrays.asList(s1, s2, s3));
        Comparator<Student> comparator = Comparator.comparing(Student::getBirthday, (d1, d2) -> {
            Long b1 = ChronoUnit.SECONDS.between(time.toInstant(), d1.toInstant());
            Long b2 = ChronoUnit.SECONDS.between(time.toInstant(), d2.toInstant());
            return b1.compareTo(b2);
        });

        Comparator<Date> dateComparator = new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        };

        list.sort(Comparator.comparing(Student::getBirthday));
        System.out.println(list);
    }

    static class Student {
        private String name;
        private int age;
        private Date birthday;

        public Student(String name, int age, Date birthday) {
            this.name = name;
            this.age = age;
            this.birthday = birthday;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", birthday=" + birthday +
                    '}';
        }
    }
}
