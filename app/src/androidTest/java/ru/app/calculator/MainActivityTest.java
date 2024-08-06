package ru.app.calculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;
import android.os.Environment;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private ActivityScenario<MainActivity> activityScenario;
    private File testFile;

    @Before
    public void setUp() {
        activityScenario = ActivityScenario.launch(MainActivity.class);

        String downloadDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        testFile = new File(downloadDirectoryPath, "myFileForCalculator.txt");
        // Удаляем файл перед каждым тестом, если он существует
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @After
    public void tearDown() {
        if (activityScenario != null) {
            activityScenario.close();
        }
    }

    @Test
    public void testAddition() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber1))
                .perform(ViewActions.replaceText("10"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber2))
                .perform(ViewActions.replaceText("5"));
        Espresso.onView(ViewMatchers.withId(R.id.buttonAdd))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.textViewResult1))
                .check(ViewAssertions.matches(ViewMatchers.withText("Результат сложения: 15.0")));
    }

    @Test
    public void testSubtraction() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber1))
                .perform(ViewActions.replaceText("10"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber2))
                .perform(ViewActions.replaceText("5"));
        Espresso.onView(ViewMatchers.withId(R.id.buttonSubtract))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.textViewResult1))
                .check(ViewAssertions.matches(ViewMatchers.withText("Результат вычитания: 5.0")));
    }

    @Test
    public void testMultiplication() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber1))
                .perform(ViewActions.replaceText("10"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber2))
                .perform(ViewActions.replaceText("5"));
        Espresso.onView(ViewMatchers.withId(R.id.buttonMultiply))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.textViewResult1))
                .check(ViewAssertions.matches(ViewMatchers.withText("Результат умножения: 50.0")));
    }

    @Test
    public void testDivision() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber1))
                .perform(ViewActions.replaceText("10"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber2))
                .perform(ViewActions.replaceText("5"));
        Espresso.onView(ViewMatchers.withId(R.id.buttonDivide))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.textViewResult1))
                .check(ViewAssertions.matches(ViewMatchers.withText("Результат деления: 2.0")));

        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber1))
                .perform(ViewActions.replaceText("5"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber2))
                .perform(ViewActions.replaceText("0"));
        Espresso.onView(ViewMatchers.withId(R.id.buttonDivide))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.textViewResult1))
                .check(ViewAssertions.matches(ViewMatchers.withText("Ошибка: Деление на ноль")));
    }

    @Test
    public void testSquareRoot() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber))
                .perform(ViewActions.replaceText("16"));
        Espresso.onView(ViewMatchers.withId(R.id.buttonSqrt))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.textViewResult2))
                .check(ViewAssertions.matches(ViewMatchers.withText("Результат: 4.0")));
    }

    @Test
    public void testPower() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumberForNumb))
                .perform(ViewActions.replaceText("2"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumberForDeg))
                .perform(ViewActions.replaceText("3"));
        Espresso.onView(ViewMatchers.withId(R.id.buttonPower))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.textViewResult3))
                .check(ViewAssertions.matches(ViewMatchers.withText("Результат: 8.0")));
    }

    @Test
    public void testEmptyInputToastLogic() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber1))
                .perform(ViewActions.replaceText(""));
        Espresso.onView(ViewMatchers.withId(R.id.editTextNumber2))
                .perform(ViewActions.replaceText("5"));
        Espresso.onView(ViewMatchers.withId(R.id.buttonAdd))
                .perform(ViewActions.click());

        //Получаем доступ к MainActivity для проверки флага
        activityScenario.onActivity(activity -> {
            assertTrue(activity.toastShown);
        });
    }

    @Test
    public void testSaveText() {
        String testResult = "Test Result";

        // Запускаем сценарий для MainActivity
        activityScenario.onActivity(activity -> {
            activity.saveText(testResult);

            // Устанавливаем время для записи в файл
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Проверяем содержимое файла
            try {
                String fileContent = readFile(testFile);
                assertEquals(testResult + "\n", fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String readFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        is.read(buffer);
        is.close();
        return new String(buffer);
    }
}
