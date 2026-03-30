package br.com.fiap.vale.validation;

import br.com.fiap.vale.exception.PercentualInvalidoException;
import br.com.fiap.vale.exception.ValeCanceladoException;
import br.com.fiap.vale.exception.ValeDuplicadoException;
import br.com.fiap.vale.exception.ValeNaoEncontradoException;
import br.com.fiap.vale.model.TipoVale;
import br.com.fiap.vale.model.Vale;
import br.com.fiap.vale.repository.IValeRepository;
import br.com.fiap.vale.util.Constantes;

import java.util.Optional;
import java.util.logging.Logger;

public class ValeValidator {

    private static final Logger LOGGER = Logger.getLogger(ValeValidator.class.getName());

    private final IValeRepository repository;

    public ValeValidator(IValeRepository repository) {
        this.repository = repository;
    }

    public void validarPercentual(double percentual, TipoVale tipoVale) {
        LOGGER.info(String.format(Constantes.LOG_VALIDANDO_PERCENTUAL, percentual, tipoVale));

        if (!tipoVale.percentualValido(percentual)) {
            throw new PercentualInvalidoException(percentual, tipoVale);
        }
    }

    public void validarValeUnicoPorMes(int funcionarioId, String nomeFuncionario, String mesAno) {
        LOGGER.info(String.format(Constantes.LOG_VALIDANDO_VALE_MES, funcionarioId, mesAno));

        if (repository.possuiValeAtivoNoMes(funcionarioId, mesAno)) {
            throw new ValeDuplicadoException(nomeFuncionario, mesAno);
        }
    }

    public Vale validarValeCancelavel(int valeId) {
        LOGGER.info(String.format(Constantes.LOG_VALIDANDO_VALE_CANCELAVEL, valeId));

        Optional<Vale> valeOpt = repository.buscarValePorId(valeId);

        if (!valeOpt.isPresent()) {
            throw new ValeNaoEncontradoException(valeId);
        }

        Vale vale = valeOpt.get();

        if (!vale.getStatus().isCancelavel()) {
            throw new ValeCanceladoException(valeId);
        }

        return vale;
    }
}