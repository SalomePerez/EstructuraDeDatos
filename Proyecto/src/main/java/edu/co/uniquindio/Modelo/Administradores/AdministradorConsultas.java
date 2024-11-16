package edu.co.uniquindio.Modelo.Administradores;

public class AdministradorConsultas {
    private final GestorProcesos gestorProcesos;

    /**
     * Constructor de la clase AdministradorConsultas.
     * @param gestorProcesos Instancia de GestorProcesos para gestionar procesos.
     */
    public AdministradorConsultas(GestorProcesos gestorProcesos) {
        this.gestorProcesos = gestorProcesos;
    }

    /**
     * Intercambia las posiciones de dos actividades en un proceso.
     * Opcionalmente, también intercambia sus tareas.
     *
     * @param idProceso           Identificador único del proceso.
     * @param nombrePrimeraActividad  Nombre de la primera actividad.
     * @param nombreSegundaActividad  Nombre de la segunda actividad.
     * @param intercambiarTareas  Indica si se deben intercambiar las tareas.
     */
    public void intercambiarActividades(UUID idProceso, String nombrePrimeraActividad, String nombreSegundaActividad, boolean intercambiarTareas) {
        Proceso proceso = gestorProcesos.buscarProceso(idProceso);
        if (proceso == null) {
            throw new IllegalStateException("Proceso no encontrado.");
        }

        ListaEnlazada<Actividad> actividades = proceso.getActividades();
        Actividad actividad1 = buscarActividadPorNombre(actividades, nombrePrimeraActividad);
        Actividad actividad2 = buscarActividadPorNombre(actividades, nombreSegundaActividad);

        if (actividad1 == null || actividad2 == null) {
            throw new IllegalArgumentException("Una o ambas actividades no fueron encontradas.");
        }

        // Almacena y copia las tareas originales de ambas actividades.
        Cola<Tarea> tareasPrimeraActividad = new Cola<>();
        Cola<Tarea> tareasSegundaActividad = new Cola<>();

        copiarTareas(actividad1.getTareas(), tareasPrimeraActividad);
        copiarTareas(actividad2.getTareas(), tareasSegundaActividad);

        // Intercambia las posiciones en la lista de actividades.
        intercambiarPosicionesEnLista(actividades, actividad1, actividad2);

        // Restaura las tareas originales si no se deben intercambiar.
        if (!intercambiarTareas) {
            actividad1.setTareas(tareasSegundaActividad);
            actividad2.setTareas(tareasPrimeraActividad);
        }
    }

    /**
     * Copia las tareas de una cola origen a una cola destino.
     *
     * @param origen Cola de tareas original.
     * @param destino Cola donde se copiarán las tareas.
     */
    private void copiarTareas(Cola<Tarea> origen, Cola<Tarea> destino) {
        Nodo<Tarea> nodoActual = origen.getNodoPrimero();
        while (nodoActual != null) {
            destino.encolar(nodoActual.getValorNodo());
            nodoActual = nodoActual.getSiguienteNodo();
        }
    }

    /**
     * Intercambia las posiciones de dos actividades en una lista enlazada.
     *
     * @param listaActividades Lista enlazada de actividades.
     * @param actividad1       Primera actividad a intercambiar.
     * @param actividad2       Segunda actividad a intercambiar.
     */
    private void intercambiarPosicionesEnLista(ListaEnlazada<Actividad> listaActividades,
                                               Actividad actividad1,
                                               Actividad actividad2) {
        Nodo<Actividad> nodoActual = listaActividades.getCabeza();
        Nodo<Actividad> nodoAnterior1 = null;
        Nodo<Actividad> nodo1 = null;
        Nodo<Actividad> nodoAnterior2 = null;
        Nodo<Actividad> nodo2 = null;
        Nodo<Actividad> nodoAnterior = null;

        // Busca los nodos correspondientes a las actividades.
        while (nodoActual != null) {
            if (nodoActual.getValorNodo().equals(actividad1)) {
                nodoAnterior1 = nodoAnterior;
                nodo1 = nodoActual;
            }
            if (nodoActual.getValorNodo().equals(actividad2)) {
                nodoAnterior2 = nodoAnterior;
                nodo2 = nodoActual;
            }
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getSiguienteNodo();
        }

        // Intercambia las referencias de los nodos.
        if (nodo1 != null && nodo2 != null) {
            Nodo<Actividad> siguiente1 = nodo1.getSiguienteNodo();
            Nodo<Actividad> siguiente2 = nodo2.getSiguienteNodo();

            if (nodo1.getSiguienteNodo() == nodo2) { // Caso especial: nodos adyacentes.
                nodo1.setSiguienteNodo(siguiente2);
                nodo2.setSiguienteNodo(nodo1);
                if (nodoAnterior1 != null) {
                    nodoAnterior1.setSiguienteNodo(nodo2);
                } else {
                    listaActividades.setCabeza(nodo2);
                }
            } else if (nodo2.getSiguienteNodo() == nodo1) {
                nodo2.setSiguienteNodo(siguiente1);
                nodo1.setSiguienteNodo(nodo2);
                if (nodoAnterior2 != null) {
                    nodoAnterior2.setSiguienteNodo(nodo1);
                } else {
                    listaActividades.setCabeza(nodo1);
                }
            } else { // Caso general.
                nodo1.setSiguienteNodo(siguiente2);
                nodo2.setSiguienteNodo(siguiente1);

                if (nodoAnterior1 != null) {
                    nodoAnterior1.setSiguienteNodo(nodo2);
                } else {
                    listaActividades.setCabeza(nodo2);
                }

                if (nodoAnterior2 != null) {
                    nodoAnterior2.setSiguienteNodo(nodo1);
                } else {
                    listaActividades.setCabeza(nodo1);
                }
            }
        }
    }

    /**
     * Busca una actividad por su nombre en una lista enlazada de actividades.
     *
     * @param listaActividades Lista de actividades donde buscar.
     * @param nombre           Nombre de la actividad buscada.
     * @return La actividad encontrada o {@code null} si no existe.
     */
    private Actividad buscarActividadPorNombre(ListaEnlazada<Actividad> listaActividades, String nombre) {
        Nodo<Actividad> nodoActual = listaActividades.getCabeza();
        while (nodoActual != null) {
            if (nodoActual.getValorNodo().getNombre().equals(nombre)) {
                return nodoActual.getValorNodo();
            }
            nodoActual = nodoActual.getSiguienteNodo();
        }
        return null;
    }


    /**
     * Busca tareas asociadas a un proceso basado en un tipo de búsqueda y un criterio.
     *
     * @param idProceso   Identificador único del proceso.
     * @param tipoBusqueda Tipo de búsqueda: desde el inicio, actividad actual o específica.
     * @param criterio    Criterio utilizado para filtrar las tareas (puede ser vacío).
     * @return Lista de tareas que coinciden con el criterio de búsqueda.
     */
    public List<Tarea> buscarTareasPorCriterio(UUID idProceso, TipoBusqueda tipoBusqueda, String criterio) {
        Proceso proceso = gestorProcesos.buscarProceso(idProceso);
        if (proceso == null) {
            throw new IllegalStateException("Proceso no encontrado.");
        }

        List<Tarea> tareasEncontradas = new ArrayList<>();
        ListaEnlazada<Actividad> actividades = proceso.getActividades();

        switch (tipoBusqueda) {
            case DESDE_INICIO:
                buscarTareasDesdeInicio(actividades, criterio, tareasEncontradas);
                break;

            case DESDE_ACTIVIDAD_ACTUAL:
                Actividad ultimaActividad = encontrarUltimaActividad(actividades);
                if (ultimaActividad != null) {
                    buscarTareasEnActividad(ultimaActividad, criterio, tareasEncontradas);
                }
                break;

            case DESDE_ACTIVIDAD_ESPECIFICA:
                Actividad actividadEspecifica = buscarActividadPorNombre(actividades, criterio);
                if (actividadEspecifica != null) {
                    buscarTareasEnActividad(actividadEspecifica, criterio, tareasEncontradas);
                }
                break;
        }

        return tareasEncontradas;
    }

    /**
     * Calcula el tiempo restante mínimo y máximo para completar un proceso.
     *
     * @param idProceso Identificador único del proceso.
     * @return Un objeto {@link TiempoProceso} con los tiempos mínimos y máximos restantes.
     */
    public TiempoProceso calcularTiempoRestanteProceso(UUID idProceso) {
        Proceso proceso = gestorProcesos.buscarProceso(idProceso);
        if (proceso == null) {
            throw new IllegalStateException("Proceso no encontrado.");
        }

        int tiempoMinimo = 0;
        int tiempoMaximo = 0;

        Nodo<Actividad> nodoActual = proceso.getActividades().getCabeza();
        while (nodoActual != null) {
            Actividad actividad = nodoActual.getValorNodo();
            Cola<Tarea> tareas = actividad.getTareas();
            Nodo<Tarea> nodoTarea = tareas.getNodoPrimero();

            while (nodoTarea != null) {
                Tarea tarea = nodoTarea.getValorNodo();

                if (actividad.isObligatoria() && tarea.isObligatoria()) {
                    tiempoMinimo += tarea.getDuracion();
                }
                tiempoMaximo += tarea.getDuracion();

                nodoTarea = nodoTarea.getSiguienteNodo();
            }

            nodoActual = nodoActual.getSiguienteNodo();
        }

        LocalDateTime ahora = LocalDateTime.now();
        Duration tiempoTranscurrido = Duration.between(proceso.getFechaInicio(), ahora);
        long minutosTranscurridos = tiempoTranscurrido.toMinutes();

        int tiempoMinimoRestante = Math.max(tiempoMinimo - (int) minutosTranscurridos, 0);
        int tiempoMaximoRestante = Math.max(tiempoMaximo - (int) minutosTranscurridos, 0);

        return new TiempoProceso(tiempoMinimoRestante, tiempoMaximoRestante);
    }

    /**
     * Busca tareas desde el inicio de la lista de actividades.
     *
     * @param actividades      Lista enlazada de actividades.
     * @param criterio         Criterio utilizado para filtrar las tareas.
     * @param tareasEncontradas Lista donde se almacenarán las tareas encontradas.
     */
    private void buscarTareasDesdeInicio(ListaEnlazada<Actividad> actividades, String criterio, List<Tarea> tareasEncontradas) {
        Nodo<Actividad> nodoActual = actividades.getCabeza();
        while (nodoActual != null) {
            buscarTareasEnActividad(nodoActual.getValorNodo(), criterio, tareasEncontradas);
            nodoActual = nodoActual.getSiguienteNodo();
        }
    }

    /**
     * Busca tareas en una actividad específica.
     *
     * @param actividad        Actividad donde buscar las tareas.
     * @param criterio         Criterio utilizado para filtrar las tareas.
     * @param tareasEncontradas Lista donde se almacenarán las tareas encontradas.
     */
    private void buscarTareasEnActividad(Actividad actividad, String criterio, List<Tarea> tareasEncontradas) {
        Cola<Tarea> tareas = actividad.getTareas();
        Nodo<Tarea> nodoTarea = tareas.getNodoPrimero();

        while (nodoTarea != null) {
            Tarea tarea = nodoTarea.getValorNodo();
            if (criterio.isEmpty() || tarea.getDescripcion().toLowerCase().contains(criterio.toLowerCase())) {
                tareasEncontradas.add(tarea);
            }
            nodoTarea = nodoTarea.getSiguienteNodo();
        }
    }

    /**
     * Encuentra la última actividad en una lista enlazada de actividades.
     *
     * @param actividades Lista enlazada de actividades.
     * @return La última actividad de la lista o {@code null} si la lista está vacía.
     */
    private Actividad encontrarUltimaActividad(ListaEnlazada<Actividad> actividades) {
        if (actividades.getCabeza() == null) {
            return null;
        }

        Nodo<Actividad> nodoActual = actividades.getCabeza();
        while (nodoActual.getSiguienteNodo() != null) {
            nodoActual = nodoActual.getSiguienteNodo();
        }
        return nodoActual.getValorNodo();
    }




}