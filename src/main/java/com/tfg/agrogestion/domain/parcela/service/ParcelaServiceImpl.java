package com.tfg.agrogestion.domain.parcela.service;

import com.tfg.agrogestion.common.enums.RolNombre;
import com.tfg.agrogestion.common.exception.BusinessException;
import com.tfg.agrogestion.common.exception.ConflictException;
import com.tfg.agrogestion.common.exception.ResourceNotFoundException;
import com.tfg.agrogestion.common.service.AccesoService;
import com.tfg.agrogestion.domain.parcela.dto.request.ActualizarParcelaRequest;
import com.tfg.agrogestion.domain.parcela.dto.request.CrearParcelaRequest;
import com.tfg.agrogestion.domain.parcela.dto.response.ParcelaResponse;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import com.tfg.agrogestion.domain.parcela.mapper.ParcelaMapper;
import com.tfg.agrogestion.domain.parcela.repository.ParcelaRepository;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import com.tfg.agrogestion.domain.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ParcelaServiceImpl implements ParcelaService {

    private final ParcelaRepository parcelaRepository;
    private final ParcelaMapper parcelaMapper;
    private final UsuarioRepository usuarioRepository;
    private final AccesoService accesoService;

    @Override
    @Transactional
    public ParcelaResponse crear(CrearParcelaRequest request,
            String emailOwner) {
        Usuario owner = accesoService.resolverUsuario(emailOwner);

        if (parcelaRepository.existsByNombreAndOwnerIdAndActivaTrue(
                request.getNombre(), owner.getId())) {
            throw new ConflictException(
                    "Ya tienes una parcela con el nombre: "
                    + request.getNombre());
        }

        Parcela parcela = Parcela.builder()
                .owner(owner)
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .superficieHa(request.getSuperficieHa())
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .municipio(request.getMunicipio())
                .provincia(request.getProvincia())
                .referenciaCatastral(request.getReferenciaCatastral())
                .activa(true)
                .build();

        return parcelaMapper.toResponse(parcelaRepository.save(parcela));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ParcelaResponse> listar(String emailUsuario,
            String busqueda, Pageable pageable) {
        Usuario usuario = accesoService.resolverUsuario(emailUsuario);

        // Admin ve todas, Owner ve las suyas, Worker ve todas
        Long ownerId = null;
        if (RolNombre.ROLE_OWNER.equals(usuario.getRol())) {
            ownerId = usuario.getId();
        }

        return parcelaRepository
                .buscarConFiltros(ownerId, busqueda, pageable)
                .map(parcelaMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ParcelaResponse obtenerPorId(Long id, String emailUsuario) {
        Parcela parcela = findParcelaOrThrow(id);
        accesoService.verificarAccesoParcela(parcela, emailUsuario);
        return parcelaMapper.toResponse(parcela);
    }

    @Override
    @Transactional
    public ParcelaResponse actualizar(Long id,
            ActualizarParcelaRequest request, String emailUsuario) {
        Parcela parcela = findParcelaOrThrow(id);
        accesoService.verificarPropietarioParcela(parcela, emailUsuario);

        if (StringUtils.hasText(request.getNombre()))
            parcela.setNombre(request.getNombre());
        if (StringUtils.hasText(request.getDescripcion()))
            parcela.setDescripcion(request.getDescripcion());
        if (request.getSuperficieHa() != null)
            parcela.setSuperficieHa(request.getSuperficieHa());
        if (request.getLatitud() != null)
            parcela.setLatitud(request.getLatitud());
        if (request.getLongitud() != null)
            parcela.setLongitud(request.getLongitud());
        if (StringUtils.hasText(request.getMunicipio()))
            parcela.setMunicipio(request.getMunicipio());
        if (StringUtils.hasText(request.getProvincia()))
            parcela.setProvincia(request.getProvincia());
        if (StringUtils.hasText(request.getReferenciaCatastral()))
            parcela.setReferenciaCatastral(request.getReferenciaCatastral());

        return parcelaMapper.toResponse(parcelaRepository.save(parcela));
    }

    @Override
    @Transactional
    public void eliminar(Long id, String emailUsuario) {
        Parcela parcela = findParcelaOrThrow(id);
        accesoService.verificarPropietarioParcela(parcela, emailUsuario);
        parcela.setActiva(false);
        parcelaRepository.save(parcela);
    }

    private Parcela findParcelaOrThrow(Long id) {
        return parcelaRepository.findByIdAndActivaTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parcela", id));
    }
}