package entities;

import exceptions.BankException;
import exceptions.SaldoInsuficiente;

public class ContaPoupanca extends Account {
    private double percentualRendimento = 1.5;
    private double taxaSaque = 1.5;

    public ContaPoupanca(String name, double balance) {
        super(name, balance);
    }

    public double getPercentualRendimento() {return percentualRendimento;}
    public void setPercentualRendimento(double percentualRendimento) {this.percentualRendimento = percentualRendimento;}

    public void aplicarRendimento(double taxaPercentual) {
        if (!isActive()) {throw new BankException("OPERAÇÃO NEGADA: Não é possível aplicar rendimento em conta bloqueada.");}
        if (taxaPercentual <= 0) {throw new BankException("ERRO: A taxa de rendimento deve ser maior que zero (Taxa informada: " + taxaPercentual + "%).");}

        double rend = getBalance() * (taxaPercentual / 100);
        double rendimento = Math.round(rend * 100.0) / 100.0;
        this.balance += rendimento;
        adicionarExtrato("RENDIMENTO APLICADO (" + taxaPercentual + "%) -> +R$" + String.format("%.2f", rendimento));
        System.out.println("Rendimento de R$" + String.format("%.2f", rendimento) + " aplicado com sucesso na conta " + getNumber());
    }

    @Override
    public boolean sacar(double valor) {
        if (!isActive()) {throw new BankException("OPERAÇÃO NEGADA: CONTA BLOQUEADA");}

        double valorTotal = valor + taxaSaque;

        if (valorTotal > getBalance()) {
            adicionarExtrato("ALERTA: SALDO INSUFICIENTE NA POUPANÇA PARA VALOR DE R$" + String.format("%.2f", valorTotal));
            throw new SaldoInsuficiente("ERRO: SALDO INSUFICIENTE NA POUPANÇA!");
        }

        this.balance -= valorTotal;
        adicionarExtrato("SAQUE POUPANÇA (Taxa: R$" + String.format("%.2f", taxaSaque) + ") -> R$" + String.format("%.2f", getBalance()));
        System.out.println("Saque realizado com sucesso na Poupança!");
        return true;
    }
}
