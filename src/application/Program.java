package application;

import entities.*;
import exceptions.BankException;

public class Program {
    public static void main(String[] args) {
        Bank unisinosBank = new Bank();

        try {
            System.out.println("=== [SISTEMA BANCÁRIO: CARGA INICIAL DE DADOS] ===");

            // Criando um mix de contas para demonstrar o Polimorfismo
            Account c1 = new ContaCorrente("Matheus Silva", 1500.0, 500.0);
            Account c2 = new ContaPoupanca("Ana Oliveira", 2500.0);
            Account c3 = new ContaCorrente("Empresa Tech", 10000.0, 2000.0);
            Account c4 = new ContaPoupanca("Reserva Emergência", 50.0);

            // Cadastrando no repositório (HashMap)
            unisinosBank.cadastrarConta(c1);
            unisinosBank.cadastrarConta(c2);
            unisinosBank.cadastrarConta(c3);
            unisinosBank.cadastrarConta(c4);

            System.out.println("\n=== [SIMULAÇÃO DE OPERAÇÕES EM TEMPO REAL] ===");

            // 1. Transferência entre diferentes tipos de conta
            System.out.println("> Operação: Transferência de Matheus para Ana");
            c1.transferir(300.0, c2);

            // 2. Cobrança de Taxa de Manutenção em lote (Interface Tributavel)
            System.out.println("> Operação: Cobrança de taxa mensal em Conta Corrente");
            ((ContaCorrente) c1).taxaManutencao();
            ((ContaCorrente) c3).taxaManutencao();

            // 3. Aplicação de rendimento em massa para todas as poupanças
            System.out.println("> Operação: Ajuste de rendimento Selic (1.5%) em Poupanças");
            ((ContaPoupanca) c2).aplicarRendimento(1.5);
            ((ContaPoupanca) c4).aplicarRendimento(1.5);

            System.out.println("\n=== [TESTE DE INTEGRIDADE E SEGURANÇA] ===");

            // 4. Tentativa de encerramento inválido (Conta com saldo)
            System.out.print("Tentando encerrar conta ativa: ");
            try {
                unisinosBank.encerrarConta(c4.getNumber());
            } catch (BankException e) {
                System.out.println("NEGADO: " + e.getMessage());
            }

            // 5. Encerramento válido (Fluxo Completo)
            System.out.println("Zerando reserva e encerrando conta...");
            double saldoFinalReserva = c4.getBalance();
            c4.sacar(saldoFinalReserva - 1.5); // Considerando taxa de saque da sua poupança
            unisinosBank.encerrarConta(c4.getNumber());

            // 6. EXIBIÇÃO DO DASHBOARD FINAL
            unisinosBank.imprimirRelatorioContas();

            System.out.printf("PATRIMÔNIO TOTAL CUSTODIADO: R$ %.2f%n",
                    unisinosBank.patrimonioTotal());

        } catch (BankException e) {
            System.err.println("\n[ERRO CRÍTICO]: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\n[ERRO DE SISTEMA]: " + e.getLocalizedMessage());
        }
    }
}