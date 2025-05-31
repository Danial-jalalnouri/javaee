package com.danialechoes.customerSystem.mapper;

import com.danialechoes.customerSystem.dto.LegalCustomerDto;
import com.danialechoes.customerSystem.dto.CustomerDto;
import com.danialechoes.customerSystem.dto.RealCustomerDto;
import com.danialechoes.customerSystem.model.LegalCustomer;
import com.danialechoes.customerSystem.model.Customer;
import com.danialechoes.customerSystem.model.RealCustomer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    RealCustomer toEntity(RealCustomerDto dto);

    RealCustomerDto toDto(RealCustomer entity);

    LegalCustomer toEntity(LegalCustomerDto dto);

    LegalCustomerDto toDto(LegalCustomer entity);

    default Customer toEntity(Object dto) {
        if (dto instanceof RealCustomerDto) {
            return toEntity((RealCustomerDto) dto);
        } else if (dto instanceof LegalCustomerDto) {
            return toEntity((LegalCustomerDto) dto);
        }
        throw new IllegalArgumentException("Unsupported DTO type: " + dto.getClass());
    }

    default CustomerDto toDto(Customer entity) {
        if (entity instanceof RealCustomer) {
            return toDto((RealCustomer) entity);
        } else if (entity instanceof LegalCustomer) {
            return toDto((LegalCustomer) entity);
        }
        throw new IllegalArgumentException("Unsupported entity type: " + entity.getClass());
    }
}
