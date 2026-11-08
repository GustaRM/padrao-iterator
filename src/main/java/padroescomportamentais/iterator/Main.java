package padroescomportamentais.iterator;

public class Main {

    public static void main(String[] args) {
        Extrato extrato = new Extrato(
                new Transacao("Salário", 5000.0, false),
                new Transacao("Transferência internacional", 15000.0, true),
                new Transacao("Supermercado", 350.5, false),
                new Transacao("Saque incomum", 9000.0, true),
                new Transacao("Conta de luz", 180.0, false)
        );

        System.out.println("--- Do mais antigo para o mais recente ---");
        for (Transacao t : extrato) {
            System.out.println(t.getDescricao() + " - R$ " + t.getValor());
        }

        System.out.println("--- Do mais recente para o mais antigo ---");
        for (Transacao t : extrato.reverso()) {
            System.out.println(t.getDescricao());
        }

        System.out.println("--- Somente suspeitas ---");
        for (Transacao t : extrato.suspeitas()) {
            System.out.println(t.getDescricao());
        }

        System.out.println("Total: " + Auditoria.contarTotalTransacoes(extrato));
        System.out.println("Suspeitas: " + Auditoria.contarTransacoesSuspeitas(extrato));
        System.out.println("Soma dos valores suspeitos: R$ " + Auditoria.somarValoresSuspeitos(extrato));
    }
}
