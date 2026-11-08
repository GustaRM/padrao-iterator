package padroescomportamentais.iterator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

/** Testa o Aggregate e o protocolo do Iterator (hasNext / next / remove). */
class ExtratoTest {

    private Extrato criarExtrato() {
        return new Extrato(
                new Transacao("Salário", 5000.0, false),
                new Transacao("Transferência internacional", 15000.0, true),
                new Transacao("Supermercado", 350.5, false),
                new Transacao("Saque incomum", 9000.0, true),
                new Transacao("Conta de luz", 180.0, false)
        );
    }

    private List<String> descricoes(Iterable<Transacao> transacoes) {
        List<String> resultado = new ArrayList<>();
        for (Transacao t : transacoes) {
            resultado.add(t.getDescricao());
        }
        return resultado;
    }

    // ---------- percurso padrão ----------

    @Test
    void percorreNaOrdemOriginal() {
        assertEquals(
                List.of("Salário", "Transferência internacional", "Supermercado", "Saque incomum", "Conta de luz"),
                descricoes(criarExtrato()));
    }

    @Test
    void usoExplicitoDeHasNextENext() {
        Iterator<Transacao> it = criarExtrato().iterator();
        assertTrue(it.hasNext());
        assertEquals("Salário", it.next().getDescricao());
        assertEquals("Transferência internacional", it.next().getDescricao());
        it.next();
        it.next();
        assertEquals("Conta de luz", it.next().getDescricao());
        assertFalse(it.hasNext());
    }

    @Test
    void nextAlemDoFimLancaNoSuchElementException() {
        Iterator<Transacao> it = new Extrato(new Transacao("Única", 1.0, false)).iterator();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void hasNextNaoAvancaOIterador() {
        Iterator<Transacao> it = criarExtrato().iterator();
        assertTrue(it.hasNext());
        assertTrue(it.hasNext());
        assertTrue(it.hasNext());
        assertEquals("Salário", it.next().getDescricao());
    }

    @Test
    void removeNaoEhSuportadoEOExtratoFicaProtegido() {
        Extrato extrato = criarExtrato();
        Iterator<Transacao> it = extrato.iterator();
        it.next();
        assertThrows(UnsupportedOperationException.class, it::remove);
        assertEquals(5, Auditoria.contarTotalTransacoes(extrato));
    }

    // ---------- reutilização e independência ----------

    @Test
    void podeSerPercorridoVariasVezes() {
        Extrato extrato = criarExtrato();
        assertEquals(descricoes(extrato), descricoes(extrato));
    }

    @Test
    void iteradoresDoMesmoExtratoSaoIndependentes() {
        Extrato extrato = criarExtrato();
        Iterator<Transacao> primeiro = extrato.iterator();
        Iterator<Transacao> segundo = extrato.iterator();

        primeiro.next();
        primeiro.next();

        assertEquals("Salário", segundo.next().getDescricao());
        assertEquals("Supermercado", primeiro.next().getDescricao());
    }

    @Test
    void iteradorDevolveOsMesmosObjetosDoExtrato() {
        Transacao original = new Transacao("Pix", 100.0, false);
        Extrato extrato = new Extrato(original);
        assertSame(original, extrato.iterator().next());
    }

    // ---------- percurso reverso ----------

    @Test
    void reversoPercorreDaMaisRecenteParaAMaisAntiga() {
        assertEquals(
                List.of("Conta de luz", "Saque incomum", "Supermercado", "Transferência internacional", "Salário"),
                descricoes(criarExtrato().reverso()));
    }

    @Test
    void reversoAlemDoFimLancaNoSuchElementException() {
        Iterator<Transacao> it = new Extrato(new Transacao("Única", 1.0, false)).reverso().iterator();
        it.next();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    // ---------- percurso filtrado ----------

    @Test
    void suspeitasDevolveSomenteTransacoesSuspeitas() {
        assertEquals(List.of("Transferência internacional", "Saque incomum"),
                descricoes(criarExtrato().suspeitas()));
    }

    @Test
    void suspeitasVazioQuandoNenhumaEhSuspeita() {
        Extrato extrato = new Extrato(new Transacao("A", 1.0, false), new Transacao("B", 2.0, false));
        assertFalse(extrato.suspeitas().iterator().hasNext());
    }

    @Test
    void suspeitasDevolveTudoQuandoTodasSaoSuspeitas() {
        Extrato extrato = new Extrato(new Transacao("A", 1.0, true), new Transacao("B", 2.0, true));
        assertEquals(List.of("A", "B"), descricoes(extrato.suspeitas()));
    }

    @Test
    void suspeitasEhAvaliadoNoMomentoDoPercurso() {
        Transacao t = new Transacao("Pix", 100.0, false);
        Extrato extrato = new Extrato(t);
        assertEquals(0, descricoes(extrato.suspeitas()).size());

        t.setSuspeita(true);
        assertEquals(List.of("Pix"), descricoes(extrato.suspeitas()));
    }

    // ---------- extrato vazio ----------

    @Test
    void extratoVazioNaoTemElementosEmNenhumPercurso() {
        Extrato vazio = new Extrato();
        assertFalse(vazio.iterator().hasNext());
        assertFalse(vazio.reverso().iterator().hasNext());
        assertFalse(vazio.suspeitas().iterator().hasNext());
        assertTrue(descricoes(vazio).isEmpty());
    }

    // ---------- proteção do Aggregate ----------

    @Test
    void alterarOArrayOriginalNaoAlteraOExtrato() {
        Transacao[] array = {new Transacao("Original", 10.0, false)};
        Extrato extrato = new Extrato(array);
        array[0] = new Transacao("Trocada", 99.0, true);
        assertEquals(List.of("Original"), descricoes(extrato));
    }

    @Test
    void transacaoNulaEhRejeitada() {
        assertThrows(NullPointerException.class, () -> new Extrato(new Transacao("A", 1.0, false), null));
    }

    @Test
    void extratoPodeSerUsadoComoIterable() {
        Iterable<Transacao> iterable = criarExtrato();
        int contador = 0;
        for (Transacao t : iterable) {
            contador++;
        }
        assertEquals(5, contador);
    }
}
