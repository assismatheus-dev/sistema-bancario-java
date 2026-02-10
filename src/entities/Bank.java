package entities;
import java.util.HashMap;
import java.util.Map;
import exceptions.BankException;

public class Bank {
    private Map<String, Account> accounts;

    public Bank() {
        this.accounts = new HashMap<>();
    }

    public void cadastrarConta(Account conta) {
        if(conta == null) {throw new BankException("CADASTRO NÃO REALIZADO -> Nenhuma conta informada");}

        if(accounts.containsKey(conta.getNumber())) {
            throw new BankException("CADASTRO NÃO REALIZADO: A conta informada (" + conta.getNumber() + ") já possui um cadastro");
        }
        accounts.put(conta.getNumber(), conta);
        System.out.println("CADASTRO REALIZADO COM SUCESSO -> " + conta.getName() + " - " + conta.getNumber());
    }

    public Account procurarConta(String numero) {
        Account conta = accounts.get(numero);
        if(conta == null) {
            throw new BankException("ALERTA: Conta não encontrada");
        }
        return conta;
    }

    public double patrimonioTotal() {
        double soma = 0.0;

        for(Account c : accounts.values()) {
            soma += c.getBalance();
        }

        return Math.round(soma * 100.0) / 100.0;
    }

    public void imprimirRelatorioContas() {
        if(accounts.isEmpty()) {
            System.out.println("=== Não há contas ativas no momento ===");
            return;
        }

        System.out.println("========== LISTAGEM GERAL DE CONTAS ==========");
        for(Account c : accounts.values()) {
            String tipo = (c instanceof ContaCorrente) ? "C. CORRENTE" : "C. POUPANÇA";
            System.out.println("CONTA: " + c.getNumber() + " | TITULAR: " + c.getName() + " | TIPO: " + tipo + " | SALDO: " + String.format("R$%.2f", c.getBalance()));
        }
        System.out.println("==============================================");
    }

    public void processarRendimentosMensais(double porcentagem) {
        //Apenas contas poupanças serão contabilizadas
        int contasProessadas = 0;

        for(Account c : accounts.values()) {
            if(c instanceof ContaPoupanca) {
                ((ContaPoupanca) c).aplicarRendimento(porcentagem);
                contasProessadas++;
            }
        }
        System.out.println("RENDIMENTO APLICADO: " + contasProessadas + "Contas poupanças processadas");
    }

    public void encerrarConta(String numero) {
        Account conta = procurarConta(numero);

        conta.validarEncerramento();

        accounts.remove(numero);
        System.out.println("REMOÇÃO CONCLUÍDA: A conta " + conta.getNumber() + " foi removida com sucesso!");
    }
}

