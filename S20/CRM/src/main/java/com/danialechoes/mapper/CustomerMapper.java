package com.danialechoes.mapper;

import com.danialechoes.dto.LegalCustomerDTO;
import com.danialechoes.dto.RealCustomerDTO;
import com.danialechoes.entity.LegalCustomer;
import com.danialechoes.entity.RealCustomer;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CustomerMapper {
    @BeanMapping(qualifiedByName = "toRealCustomer")
    RealCustomer toEntity(RealCustomerDTO dto);

    @BeanMapping(qualifiedByName = "toLegalCustomer")
    LegalCustomer toEntity(LegalCustomerDTO dto);

    @Named("toRealCustomer")
    @InheritConfiguration
    RealCustomerDTO toDto(RealCustomer entity);

    @Named("toLegalCustomer")
    @InheritConfiguration
    LegalCustomerDTO toDto(LegalCustomer entity);

    @InheritConfiguration
    void updateRealCustomer(@MappingTarget RealCustomer entity, RealCustomerDTO dto);

    @InheritConfiguration
    void updateLegalCustomer(@MappingTarget LegalCustomer entity, LegalCustomerDTO dto);
}
