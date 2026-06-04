package com.desafio.sgs.repository;

import com.desafio.sgs.dto.SolicitacaoListaDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolicitacaoRepositoryCustomImpl implements SolicitacaoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<SolicitacaoListaDTO> listarComFiltros(String status, Integer categoriaId, LocalDate dataInicio, LocalDate dataFim) {
        StringBuilder sql = new StringBuilder("""
                SELECT s.id, st.nome AS nome_solicitante, st.cpf_cnpj, c.nome AS nome_categoria,
                       s.status, s.valor, s.data_solicitacao
                FROM solicitacao s
                JOIN solicitante st ON s.solicitante_id = st.id
                JOIN categoria c ON s.categoria_id = c.id
                WHERE 1=1
                """);

        Map<String, Object> parametros = new HashMap<>();

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND s.status = :status ");
            parametros.put("status", status);
        }

        if (categoriaId != null) {
            sql.append("AND s.categoria_id = :categoriaId ");
            parametros.put("categoriaId", categoriaId);
        }

        if (dataInicio != null) {
            sql.append("AND s.data_solicitacao >= :dataInicio ");
            parametros.put("dataInicio", dataInicio.atStartOfDay());
        }

        if (dataFim != null) {
            sql.append("AND s.data_solicitacao <= :dataFim ");
            parametros.put("dataFim", dataFim.atTime(23, 59, 59));
        }

        sql.append("ORDER BY s.data_solicitacao DESC");

        Query query = entityManager.createNativeQuery(sql.toString());
        parametros.forEach(query::setParameter);

        List<Object[]> linhas = query.getResultList();
        List<SolicitacaoListaDTO> dtos = new ArrayList<>();

        for (Object[] col : linhas) {
            LocalDateTime dataSolicitacao;
            if (col[6] instanceof Timestamp ts) {
                dataSolicitacao = ts.toLocalDateTime();
            } else {
                dataSolicitacao = (LocalDateTime) col[6];
            }

            dtos.add(new SolicitacaoListaDTO(
                    ((Number) col[0]).intValue(),
                    (String) col[1],
                    (String) col[2],
                    (String) col[3],
                    (String) col[4],
                    (BigDecimal) col[5],
                    dataSolicitacao
            ));
        }

        return dtos;
    }
}