package com.clinicaoftalmologica.clinicaoftalmologicabackend.model;

    import jakarta.persistence.*;

    import java.time.LocalDateTime;

    @Entity
    @Table(name = "bitacora")
    public class Bitacora {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "usuario_id", nullable = false)
        private Usuario usuario;

        @Column(nullable = false)
        private String accion;

        @Column(nullable = false)
        private String entidad;

        @Column
        private Long entidadId;

        @Column(columnDefinition = "TEXT")
        private String detalles;

        @Column(nullable = false)
        private LocalDateTime fecha;

        @Column
        private String ip;

        public Bitacora() {
        }
        
        public Bitacora(Usuario usuario, String accion, String entidad, Long entidadId, String detalles, String ip) {
            this.usuario = usuario;
            this.accion = accion;
            this.entidad = entidad;
            this.entidadId = entidadId;
            this.detalles = detalles;
            this.ip = ip;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public String getAccion() {
            return accion;
        }

        public void setAccion(String accion) {
            this.accion = accion;
        }

        public String getEntidad() {
            return entidad;
        }

        public void setEntidad(String entidad) {
            this.entidad = entidad;
        }

        public Long getEntidadId() {
            return entidadId;
        }

        public void setEntidadId(Long entidadId) {
            this.entidadId = entidadId;
        }

        public String getDetalles() {
            return detalles;
        }

        public void setDetalles(String detalles) {
            this.detalles = detalles;
        }

        public LocalDateTime getFecha() {
            return fecha;
        }

        public void setFecha(LocalDateTime fecha) {
            this.fecha = fecha;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }