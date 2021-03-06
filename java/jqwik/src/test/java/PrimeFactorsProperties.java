import static org.apache.commons.math3.primes.Primes.isPrime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import net.jqwik.api.Assume;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.Report;
import net.jqwik.api.Reporting;
import net.jqwik.api.constraints.IntRange;
import org.apache.commons.math3.primes.Primes;

class PrimeFactorsProperties {

    private static final int FIRST_PRIME_NUMBER = 2;

    @Property
    @Report(Reporting.GENERATED)
    @Label("all factors are primes")
    void allFactorsArePrime(@ForAll @IntRange(min = FIRST_PRIME_NUMBER) int anInt) {
        List<Integer> primeFactors = primeFactors(anInt);

        primeFactors.forEach(
                factor -> assertThat(isPrime(factor)).isTrue()
        );
    }

    @Property
    @Label("multiply all factors")
    void multiplyAllFactors(@ForAll @IntRange(min = FIRST_PRIME_NUMBER) int anInt) {
        List<Integer> primeFactors = primeFactors(anInt);

        Integer allMultiply = primeFactors.stream()
                .reduce(1, (a, b) -> a * b);
        assertThat(allMultiply).isEqualTo(anInt);
    }

    @Property
    @Label("a prime number is the only prime factor")
    void primeIsOnlyFactor(@ForAll @IntRange(min = FIRST_PRIME_NUMBER) int anInt) {
        Assume.that(Primes.isPrime(anInt));

        List<Integer> primeFactor = primeFactors(anInt);
        assertThat(primeFactor).containsExactly(anInt);
    }

    @Property
    @Label("0 and 1 have have no prime factors")
    void noPrimeFactors(@ForAll @IntRange(max = 1) int anInt) {
        assertThat(primeFactors(anInt)).isEmpty();
    }

    @Property
    @Label("IAE for numbers < 0")
    void iaeForNegativeNumbers(@ForAll @IntRange(min = Integer.MIN_VALUE, max = -1) int anInt) {
        assertThatThrownBy(() -> primeFactors(anInt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(anInt + " is an invalid input.");
    }

    List<Integer> primeFactors(int number) {
        if (number < 0) {
            throw new IllegalArgumentException(number + " is an invalid input.");
        }
        if (number < 2) {
            return Collections.emptyList();
        }
        List<Integer> integers = Primes.primeFactors(number);
        System.out.println(number + " -> " + integers + "\n");
        return integers;

    }

}
