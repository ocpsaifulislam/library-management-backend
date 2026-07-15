package dev.shoaibsuad.library_management.common.service.serviceImpl;

import dev.shoaibsuad.library_management.common.service.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdGeneratorServiceImpl implements IdGeneratorService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long getNewId(String tableName) {

        String sql = "SELECT S_" + tableName + ".NEXTVAL FROM DUAL";

        Long id = jdbcTemplate.queryForObject(sql, Long.class);

        if (id == null) {
            throw new IllegalStateException("Sequence not found for table: " + tableName);
        }

        return id;
    }
}