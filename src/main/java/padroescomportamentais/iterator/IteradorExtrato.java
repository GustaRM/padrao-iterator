package padroescomportamentais.iterator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/** Papel "ConcreteIterator": percorre a lista do início ao fim, guardando a posição atual. */
public class IteradorExtrato implements Iterator<Transacao> {

    private final List<Transacao> transacoes;
    private int indice = 0;

    public IteradorExtrato(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }

    @Override
    public boolean hasNext() {
        return indice < transacoes.size();
    }

    @Override
    public Transacao next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Não há mais transações");
        }
        return transacoes.get(indice++);
    }

    // remove() não é implementado: o método padrão da interface lança
    // UnsupportedOperationException, protegendo o extrato contra alterações.
}
