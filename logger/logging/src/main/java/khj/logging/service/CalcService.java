package khj.logging.service;

import khj.logging.exception.customException;
import org.springframework.stereotype.Service;

@Service
public class CalcService {
    public int add(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }

    public int mul(int a, int b) {
        return a * b;
    }

    public int div(int a, int b) throws customException {
        if (b == 0) {
            throw new customException("b cannot be zero");
        }
        return a / b;
    }

    public int mod(int a, int b) {
        return a % b;
    }
}
