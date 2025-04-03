package br.com.gramado.parkingapp.command.payment;

import br.com.gramado.parkingapp.entity.Payment;
import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.service.parkingpayment.PaymentServiceInterface;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.enums.TypePayment;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class InsertPaymentCommand {

    @Resource
    private PaymentServiceInterface parkingPaymentService;

    public Payment execute(PriceTable priceTable, TypePayment typePayment) throws ValidationsException {
        Payment payment = new Payment();

        validatePaymentAndTypeCharge(priceTable.getTypeCharge(), typePayment);

        payment.setPaymentType(typePayment);
        payment.setPaid(false);

        if (TypeCharge.FIXED.equals(priceTable.getTypeCharge())) {
            payment.setPrice(priceTable.getValue());
        }

        return parkingPaymentService.insert(payment);
    }

    private void validatePaymentAndTypeCharge(TypeCharge typeCharge, TypePayment preferentialPayment) throws ValidationsException {
        if (TypePayment.PIX.equals(preferentialPayment) && !TypeCharge.FIXED.equals(typeCharge)) {
            throw new ValidationsException("Op\u00E7\u00E3o de pagamento PIX est\u00E1 dispon\u00EDvel apenas para per\u00EDodos fixos!");
        }
    }
}