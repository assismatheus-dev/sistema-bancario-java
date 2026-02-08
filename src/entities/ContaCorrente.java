package entities;
import exceptions.BankException;
import exceptions.SaldoInsuficiente;
import interfaces.Tributavel;

public class ContaCorrente extends Account implements Tributavel {
    private double limite;
    private double taxaSaque = 3.5;

    public ContaCorrente(String name, double balance, double limite) {
        super(name, balance);
        this.limite = limite;
    }

    public double getLimite() {return limite;}
    public void setLimite(double limite) {this.limite = limite;}

    @Override
    public void taxaManutencao() {
        if(!isActive()) {throw new BankException("FALHA: Não é possível cobrar taxa de conta bloqueada");}

        double taxa = 10.00;
        this.balance -= taxa;
        adicionarExtrato("TAXA DE MANUTENÇÃO MENSAL: R$" + String.format("%.2f", taxa));
        System.out.println("Taxa de manutenção aplicada na conta: " + getNumber());
    }

    @Override
    public boolean sacar(double valor) {
        if (!isActive()) {throw new BankException("OPERAÇÃO NEGADA: CONTA BLOQUEADA");}

        double valorTotal = valor + taxaSaque;

        if (valorTotal > getBalance() + limite) {
            adicionarExtrato("ALERTA: SALDO INSUFICIENTE PARA SAQUE DE R$" + String.format("%.2f", valorTotal));
            throw new SaldoInsuficiente("ERRO: SALDO INSUFICIENTE (Saldo + Limite excedido)!");
        }

        this.balance -= valorTotal;
        adicionarExtrato("SAQUE COM TAXA (R$" + String.format("%.2f", valorTotal) + ") -> R$" + String.format("%.2f", getBalance()));
        System.out.println("Saque realizado com sucesso!");
        return true;
    }
}
