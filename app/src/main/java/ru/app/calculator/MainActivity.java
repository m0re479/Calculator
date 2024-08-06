package ru.app.calculator;

import static java.lang.Math.pow;

import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private File file;

    EditText editTextNumber1, editTextNumber2, editTextNumber,
            editTextNumberForNumb,
            editTextNumberForDeg;
    TextView textViewResult1, textViewResult2, textViewResult3;

    public boolean toastShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Инициализация компонентов UI
        initializeUI();

        // Обработка нажатий кнопок
        setupListeners();
    }

    private void initializeUI() {
        editTextNumber1 = findViewById(R.id.editTextNumber1);
        editTextNumber2 = findViewById(R.id.editTextNumber2);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextNumberForNumb = findViewById(R.id.editTextNumberForNumb);
        editTextNumberForDeg = findViewById(R.id.editTextNumberForDeg);
        textViewResult1 = findViewById(R.id.textViewResult1);
        textViewResult2 = findViewById(R.id.textViewResult2);
        textViewResult3 = findViewById(R.id.textViewResult3);
    }

    private void setupListeners() {
        findViewById(R.id.buttonAdd).setOnClickListener(v -> calculate('+'));
        findViewById(R.id.buttonSubtract).setOnClickListener(v -> calculate('-'));
        findViewById(R.id.buttonMultiply).setOnClickListener(v -> calculate('*'));
        findViewById(R.id.buttonDivide).setOnClickListener(v -> calculate('/'));
        findViewById(R.id.buttonSqrt).setOnClickListener(v -> calculate('√'));
        findViewById(R.id.buttonPower).setOnClickListener(v -> calculate('^'));
    }

    protected void calculate(char operator) {
        String number1Str, number2Str, numberStr, numberForNumbStr, numberForDegStr;

        // Получение текста из EditText
        number1Str = editTextNumber1.getText().toString();
        number2Str = editTextNumber2.getText().toString();
        numberStr = editTextNumber.getText().toString();
        numberForNumbStr = editTextNumberForNumb.getText().toString();
        numberForDegStr = editTextNumberForDeg.getText().toString();

        double num1 = 0, num2 = 0, num = 0, numForNumb = 0, numForDeg = 0;
        double result = 0;

        try {
            if (operator != '√' && operator != '^') {
                if (number1Str.isEmpty() || number2Str.isEmpty()) {
                    showToast("Пожалуйста, введите оба числа");
                } else {
                    num1 = Double.parseDouble(number1Str);
                    num2 = Double.parseDouble(number2Str);
                    switch (operator) {
                        case '+':
                            result = num1+num2;
                            textViewResult1.setText("Результат сложения: " + result);
                            break;
                        case '-':
                            result = num1-num2;
                            textViewResult1.setText("Результат вычитания: " + result);
                            break;
                        case '*':
                            result = num1*num2;
                            textViewResult1.setText("Результат умножения: " + result);
                            break;
                        case '/':
                            if (num2 != 0) {
                                result = num1 / num2;
                                textViewResult1.setText("Результат деления: " + result);
                            } else textViewResult1.setText("Ошибка: Деление на ноль");
                            break;
                    }
                    saveText(num1 + operator + num2 + "=" + result);
                }
            } else {
                if (operator == '^') {
                    if (numberForNumbStr.isEmpty() || numberForDegStr.isEmpty()) {
                        showToast("Пожалуйста, введите число и степень");
                    } else {
                        numForNumb = Double.parseDouble(numberForNumbStr);
                        numForDeg = Double.parseDouble(numberForDegStr);
                        result = pow(numForNumb, numForDeg);
                        textViewResult3.setText("Результат: " + result);
                        saveText(numForNumb + "^" + numForDeg + "=" + result);
                    }
                } else {
                    if (numberStr.isEmpty()) {
                        showToast("Пожалуйста, введите число");
                    } else {
                        num = Double.parseDouble(numberStr);
                        result = Math.sqrt(num);
                        textViewResult2.setText("Результат: " + result);
                        saveText("√" + num + "=" + result);
                    }
                }
            }
        } catch (NumberFormatException e) {
            showToast("Ошибка: неверный формат числа");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        toastShown = true;
    }

    public void saveText(String result) {
        FileOutputStream fos = null;
        String downloadDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        file = new File(downloadDirectoryPath, "myFileForCalculator.txt");
        try {
            fos = new FileOutputStream(file, true);
            String text = result + "\n";
            fos.write(text.getBytes());
            showToast("Результат сохранён");
        } catch (IOException ex) {
            showToast(ex.getMessage());
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException ex) {
                showToast(ex.getMessage());
            }
        }
    }
}