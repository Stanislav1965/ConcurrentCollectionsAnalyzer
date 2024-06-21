import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    // Создаем из символов "abc" 10 000 текстов, длиной 100 000 каждый
    // Длинна строки 100 000
    private static final int LENGTH_STRING = 100_000;
    // Количество текстов 10 000
    private static final int COUNT_TEXTS = 10_000;
    //Глубина очереди
    private static final int CAPACITY_QUEUE = 10_000;

    public static void main(String[] args)  {

        ArrayBlockingQueue<String> queueA = new ArrayBlockingQueue<>(CAPACITY_QUEUE);
        ArrayBlockingQueue<String> queueB = new ArrayBlockingQueue<>(CAPACITY_QUEUE);
        ArrayBlockingQueue<String> queueC = new ArrayBlockingQueue<>(CAPACITY_QUEUE);

        String[] texts = new String[COUNT_TEXTS];

        // Создадим поток для заполнения очереди текстами
        new Thread(null, () -> {
            for (int i = 0; i < texts.length; i++) {
                texts[i] = generateText("abc", LENGTH_STRING);
                try {
                    queueA.put(texts[i]);
                    queueB.put(texts[i]);
                    queueC.put(texts[i]);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        ///Поток подсчитывающий символы b
        new Thread(null, () -> {
            int max = 0;
            for (int i = 0; i < texts.length; i++) {
                try {
                    String string = queueB.take();
                    long count = string.chars().filter(ch -> ch == 'a').count();
                    if ((int) count > max) {
                        max = (int) count;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println("Максимальное количество вхождений a : " + max);
        }).start();

        //Поток подсчитывающий символы b
        new Thread(null, () -> {
            int max = 0;
            for (int i = 0; i < texts.length; i++) {
                try {
                    String string = queueA.take();
                    long count = string.chars().filter(ch -> ch == 'b').count();
                    if ((int) count > max) {
                        max = (int) count;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println("Максимальное количество вхождение b : " + max);
        }).start();

        //Поток подсчитывающий символы c
        new Thread(null, () -> {
            int max = 0;
            for (int i = 0; i < texts.length; i++) {
                try {
                    String string = queueC.take();
                    long count = string.chars().filter(ch -> ch == 'c').count();
                    if ((int) count > max) {
                        max = (int) count;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println("Максимальное количество вхождение c : " + max);
        }).start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
