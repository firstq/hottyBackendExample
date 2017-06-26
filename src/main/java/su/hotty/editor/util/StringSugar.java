package su.hotty.editor.util;

/**
 * Вспомогательный класс для работы со строками.
 */
public class StringSugar {

    private final StringBuilder stringBuilder = new StringBuilder();

    public StringSugar(String... strings) {
        a(strings);
    }

    /**
     * Добавляет переданные в качестве аргумента строки к хранимой внутри себя строке.
     *
     * @param strings Строки для добавляния.
     * @return Себя.
     */
    public StringSugar a(String... strings) {
        for (String s : strings) {
            stringBuilder.append(s);
        }
        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
