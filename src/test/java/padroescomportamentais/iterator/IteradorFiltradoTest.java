package padroescomportamentais.iterator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

/** O IteradorFiltrado é genérico: aqui é testado com Integer, sem depender de Transacao. */
class IteradorFiltradoTest {

    private Iterator<Integer> pares(Integer... numeros) {
        return new IteradorFiltrado<>(Arrays.asList(numeros).iterator(), n -> n % 2 == 0);
    }

    @Test
    void devolveSomenteOsElementosQueAtendemAoFiltro() {
        Iterator<Integer> it = pares(1, 2, 3, 4, 5, 6);
        assertEquals(2, it.next());
        assertEquals(4, it.next());
        assertEquals(6, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void semCorrespondenciaNaoTemElementos() {
        Iterator<Integer> it = pares(1, 3, 5);
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void origemVaziaNaoTemElementos() {
        assertFalse(pares().hasNext());
    }

    @Test
    void hasNextRepetidoNaoPulaElementos() {
        Iterator<Integer> it = pares(1, 2, 3, 4);
        assertTrue(it.hasNext());
        assertTrue(it.hasNext());
        assertEquals(2, it.next());
        assertTrue(it.hasNext());
        assertEquals(4, it.next());
    }

    @Test
    void nextFuncionaSemChamarHasNextAntes() {
        Iterator<Integer> it = pares(1, 2, 3, 4);
        assertEquals(2, it.next());
        assertEquals(4, it.next());
    }

    @Test
    void nextAlemDoFimLancaExcecao() {
        Iterator<Integer> it = pares(2);
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void removeNaoEhSuportado() {
        Iterator<Integer> it = pares(2, 4);
        it.next();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    void funcionaComQualquerTipoDeElemento() {
        Iterator<String> it = new IteradorFiltrado<>(
                Arrays.asList("ana", "bia", "alice", "caio").iterator(), s -> s.startsWith("a"));
        assertEquals("ana", it.next());
        assertEquals("alice", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void parametrosNulosSaoRejeitados() {
        assertThrows(NullPointerException.class, () -> new IteradorFiltrado<Integer>(null, n -> true));
        assertThrows(NullPointerException.class,
                () -> new IteradorFiltrado<Integer>(Collections.<Integer>emptyList().iterator(), null));
    }
}
