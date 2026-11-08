package padroescomportamentais.iterator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/** Papel "ConcreteIterator": percorre a lista do fim para o início. */
public class IteradorExtratoReverso implements Iterator<Transacao> {

    private final List<Transacao> transacoes;
    private int indice;

    public IteradorExtratoReverso(List<Transacao> transacoes) {
        this.transacoes = transacoes;
        this.indice = transacoes.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return indice >= 0;
    }

    @Override
    public Transacao next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Não há mais transações");
        }
        return transacoes.get(indice--);
    }
}
