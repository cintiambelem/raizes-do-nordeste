package br.com.raizesdonordeste.domain;

public final class Enums {
    private Enums() {}

    public enum Role { CLIENTE, ATENDENTE, COZINHA, GERENTE, ADMIN }
    public enum Channel { APP, TOTEM, BALCAO, PICKUP, WEB }
    public enum OrderStatus { AGUARDANDO_PAGAMENTO, EM_PREPARO, PRONTO, ENTREGUE, CANCELADO }
    public enum PaymentStatus { APROVADO, RECUSADO }
}
