package padroescomportamentais.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Iterator que envolve outro Iterator e só devolve os elementos que
 * satisfazem um filtro. Funciona com qualquer tipo de elemento.
 */
public class IteradorFiltrado<T> implements Iterator<T> {

    private final Iterator<T> origem;
    private final Predicate<T> filtro;
    private T proximo;
    private boolean temProximo = false;

    public IteradorFiltrado(Iterator<T> origem, Predicate<T> filtro) {
        this.origem = Objects.requireNonNull(origem, "O iterador de origem é obrigatório");
        this.filtro = Objects.requireNonNull(filtro, "O filtro é obrigatório");
    }

    @Override
    public boolean hasNext() {
        // procura (uma vez) o próximo elemento aceito; chamar hasNext() várias vezes não avança
        while (!temProximo && origem.hasNext()) {
            T candidato = origem.next();
            if (filtro.test(candidato)) {
                proximo = candidato;
                temProximo = true;
            }
        }
        return temProximo;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Não há mais elementos que atendam ao filtro");
        }
        T resultado = proximo;
        proximo = null;
        temProximo = false;
        return resultado;
    }
}
