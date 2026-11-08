package padroescomportamentais.iterator;

import java.util.Iterator;

/** Cliente do padrão (equivale ao Censo do exemplo): percorre o extrato sem conhecer sua estrutura. */
public class Auditoria {

    public static int contarTransacoesSuspeitas(Extrato extrato) {
        int quantidade = 0;
        for (Transacao transacao : extrato) {
            if (transacao.isSuspeita()) {
                quantidade++;
            }
        }
        return quantidade;
    }

    public static int contarTotalTransacoes(Extrato extrato) {
        int quantidade = 0;
        for (Iterator<Transacao> t = extrato.iterator(); t.hasNext(); ) {
            quantidade++;
            t.next();
        }
        return quantidade;
    }

    public static double somarValores(Extrato extrato) {
        double total = 0;
        for (Transacao transacao : extrato) {
            total += transacao.getValor();
        }
        return total;
    }

    /** Usa o percurso filtrado: o if desaparece do cliente. */
    public static double somarValoresSuspeitos(Extrato extrato) {
        double total = 0;
        for (Transacao transacao : extrato.suspeitas()) {
            total += transacao.getValor();
        }
        return total;
    }

    /** Usa o percurso inverso: a primeira transação do percurso é a mais recente. */
    public static Transacao ultimaTransacao(Extrato extrato) {
        Iterator<Transacao> iterador = extrato.reverso().iterator();
        return iterador.hasNext() ? iterador.next() : null;
    }
}
