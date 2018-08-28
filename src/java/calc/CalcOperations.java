package calc;

public class CalcOperations {

    private static double add(double a, double b) {
        return a + b;
    }

    private static double subtract(double a, double b) {
        return a - b;
    }

    private static double multiply(double a, double b) {
        return a * b;
    }

    private static double divide(double a, double b) {
        return a / b;
    }

    /**
     * calcResult - калькуляция
     *
     * @param operType OperationType
     * @param one double
     * @param two double
     * @return double
     */
    public double calcResult(OperationType operType, double one, double two) {
        double result = 0;
        switch (operType) {
            case ADD: {
                result = add(one, two);
                break;
            }
            case SUBTRACT: {
                result = subtract(one, two);
                break;
            }
            case DIVIDE: {
                result = divide(one, two);
                break;
            }
            case MULTIPLY: {
                result = multiply(one, two);
                break;
            }
        }
        return result;
    }
}
