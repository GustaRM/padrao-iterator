package padroescomportamentais.iterator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Equivale ao CensoTest do exemplo. */
class AuditoriaTest {

    private static final double DELTA = 0.001;

    private Extrato criarExtrato() {
        return new Extrato(
                new Transacao("Salário", 5000.0, false),
                new Transacao("Transferência internacional", 15000.0, true),
                new Transacao("Supermercado", 350.5, false),
                new Transacao("Saque incomum", 9000.0, true),
                new Transacao("Conta de luz", 180.0, false)
        );
    }

    @Test
    void deveContarTransacoesSuspeitas() {
        assertEquals(2, Auditoria.contarTransacoesSuspeitas(criarExtrato()));
    }

    @Test
    void deveContarTotalTransacoes() {
        assertEquals(5, Auditoria.contarTotalTransacoes(criarExtrato()));
    }

    @Test
    void deveSomarTodosOsValores() {
        assertEquals(29530.5, Auditoria.somarValores(criarExtrato()), DELTA);
    }

    @Test
    void deveSomarApenasValoresSuspeitos() {
        assertEquals(24000.0, Auditoria.somarValoresSuspeitos(criarExtrato()), DELTA);
    }

    @Test
    void deveRetornarAUltimaTransacao() {
        assertEquals("Conta de luz", Auditoria.ultimaTransacao(criarExtrato()).getDescricao());
    }

    @Test
    void extratoVazioNaoGeraErros() {
        Extrato vazio = new Extrato();
        assertEquals(0, Auditoria.contarTransacoesSuspeitas(vazio));
        assertEquals(0, Auditoria.contarTotalTransacoes(vazio));
        assertEquals(0.0, Auditoria.somarValores(vazio), DELTA);
        assertEquals(0.0, Auditoria.somarValoresSuspeitos(vazio), DELTA);
        assertNull(Auditoria.ultimaTransacao(vazio));
    }

    @Test
    void contagemRefleteMudancasNasTransacoes() {
        Extrato extrato = criarExtrato();
        extrato.iterator().next().setSuspeita(true); // "Salário" passa a ser suspeita
        assertEquals(3, Auditoria.contarTransacoesSuspeitas(extrato));
    }

    @Test
    void doisPercursosNaMesmaAuditoriaNaoSeInterferem() {
        Extrato extrato = criarExtrato();
        assertEquals(5, Auditoria.contarTotalTransacoes(extrato));
        assertEquals(5, Auditoria.contarTotalTransacoes(extrato));
        assertEquals(2, Auditoria.contarTransacoesSuspeitas(extrato));
    }
}
