package entities;
import exceptions.BankException;

import java.util.List;
import java.util.ArrayList;

public abstract class Account {
    private String name;
    private String number;
    protected double balance;
    private boolean active = true;
    private ArrayList<String> extracts;

    public Account(String name, double balance) {
        if (name == null || name.trim().isEmpty()) {
            throw new BankException("ERRO: O nome do titular é obrigatório.");
        }
        if (balance < 0) {
            throw new BankException("ERRO: Saldo inicial não pode ser negativo.");
        }
        this.number = BankManager.generateNumber();
        this.name = name;
        this.balance = balance;
        this.extracts = new ArrayList<>();
        System.out.println("CONTA CRIADA - TITULAR: " + this.name);
    }

    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public String getNumber() {return number;}

    public double getBalance() {return balance;}

    public void depositar(double valor) {
        if (!isActive()) {throw new BankException("OPERAÇÃO NEGADA: A conta " + number + " está bloqueada.");}
        if (valor <= 0) {throw new BankException("VALOR INVÁLIDO: O depósito deve ser maior que zero.");}

        this.balance += valor;
        adicionarExtrato("DEPOSITO (R$" + String.format("%.2f", valor) + ") -> R$" + String.format("%.2f", getBalance()));
        System.out.println("Depósito realizado com sucesso na conta " + number + "!");
    }

    public abstract boolean sacar(double valor);

    public boolean transferir(double valor, Account destinatario) {
        if (!isActive()) {throw new BankException("OPERAÇÃO NEGADA: CONTA BLOQUEADA");}
        if (valor <= 0) {throw new BankException("VALOR INVÁLIDO: O valor da transferência deve ser positivo.");}

        try {
            this.sacar(valor);
            destinatario.depositar(valor);
            adicionarExtrato("TRANSFERÊNCIA REALIZADA (" + destinatario.getName() + " - " + destinatario.getNumber() + ") -> R$" + String.format("%.2f", valor));
            return true;

        } catch (BankException e) {
            adicionarExtrato("FALHA NA TRANSFERÊNCIA: " + e.getMessage());
            throw e;
        }
    }

    protected void adicionarExtrato(String mensagem) {
        extracts.add(mensagem);
    }

    public void exibirExtrato() {
        System.out.println("----------EXTRATO----------" + "\n" + "TITULAR: " + name + "\nCONTA: " + number + "\n");
        for(String transacao : extracts) {
            System.out.println("- " + transacao);
        }
        System.out.println("\nSALDO ATUAL: R$" + String.format("%.2f", getBalance()));
    }

    public boolean isActive() {return active;}

    public void alterarStatusConta() {
        this.active = !this.active;
        String status = this.active ? "ATIVA" : "BLOQUEADA";
        System.out.println("CONTA " + getNumber() + ": " + status);
        adicionarExtrato("ALTERAÇÃO NO STATUS DA CONTA: " + status);
    }

    public void validarEncerramento() {
        if(this.balance > 0) {throw new BankException("ERRO: A conta não pode ser encerrada pois ainda possui saldo");}
        if(this.balance < 0) {throw new BankException("ERRO: A conta não pode ser encerrada pois possui pendências");}

        //System.out.println("ENCERRAMENTO PERMITIDO");
    }
}
