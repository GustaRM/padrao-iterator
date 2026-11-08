package padroescomportamentais.iterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Papel "Aggregate" do Iterator (equivale ao Curso do exemplo).
 * Guarda as transações e oferece três formas de percorrê-las,
 * sem expor a lista interna.
 */
public class Extrato implements Iterable<Transacao> {

    private final List<Transacao> transacoes;

    public Extrato(Transacao... transacoes) {
        Objects.requireNonNull(transacoes, "As transações são obrigatórias");
        for (Transacao transacao : transacoes) {
            Objects.requireNonNull(transacao, "Transação nula não é permitida");
        }
        this.transacoes = new ArrayList<>(Arrays.asList(transacoes)); // cópia defensiva
    }

    /** Percurso padrão: da transação mais antiga para a mais recente. */
    @Override
    public Iterator<Transacao> iterator() {
        return new IteradorExtrato(transacoes);
    }

    /** Percurso inverso: da mais recente para a mais antiga. */
    public Iterable<Transacao> reverso() {
        return () -> new IteradorExtratoReverso(transacoes);
    }

    /** Percurso filtrado: apenas as transações marcadas como suspeitas. */
    public Iterable<Transacao> suspeitas() {
        return () -> new IteradorFiltrado<>(iterator(), Transacao::isSuspeita);
    }
}
