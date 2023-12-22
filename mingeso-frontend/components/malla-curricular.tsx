"use client";

import InscribirAsignaturaForm from "./inscribir-asignatura-form";
import { useEffect, useState } from "react";
import { Button } from "./ui/button"
 
export default function MallaCurricular({asignaturas, asignaturasCursadas, rut, nivel, handleAsignaturasInscritas, asignaturasInscritas, setAsignaturasInscritas}: 
    {asignaturas: any[], asignaturasCursadas: any[], rut: string, nivel:number, handleAsignaturasInscritas: Function, asignaturasInscritas: any[], setAsignaturasInscritas: Function}) {
  const maxNivel = asignaturas[asignaturas.length - 1].nivel;
  const niveles = Array.from({length: maxNivel}, (_, index) => index + 1);  
  const asignaturasNivel = asignaturas.filter(a => a.nivel == nivel);
  const [asignaturaSeleccionada, setAsignaturaSeleccionada] = useState(null);
  const [codigosAsignaturasInscritas, setCodigosAsignaturasInscritas] = useState(asignaturasCursadas.filter(ac => ac.idEstado == 1).map(ac => ac.codigoAsignatura));
  const [showModal, setShowModal] = useState(false);
  const [highlighted, setHighlighted] = useState([]);

  useEffect(() => {
    setCodigosAsignaturasInscritas(asignaturasCursadas.filter(ac => ac.idEstado == 1).map(ac => ac.codigoAsignatura));    
  }, [rut])

  function obtenerEstiloAsignatura(codigoAsignatura: number){
    const asignaturaCursada = asignaturasCursadas.find(ac => ac.codigoAsignatura == codigoAsignatura); 
    const asignaturaInscrita = asignaturasInscritas.find(ai => ai.codigo == codigoAsignatura);

    if(highlighted?.includes(codigoAsignatura)) return "bg-yellow-200 text-yellow-600 font-medium hover:bg-yellow-400 hover:text-white";
    if(asignaturaInscrita != null) return "bg-blue-200 text-blue-600 font-medium hover:bg-blue-400 hover:text-white";    
    if(asignaturaCursada == null) return "bg-gray-200 text-gray-600 hover:bg-gray-400 hover:text-white";
    if(asignaturaCursada.idEstado == 2) return "bg-red-200 text-red-600 font-medium hover:bg-red-400 hover:text-white";
    if(asignaturaCursada.idEstado == 3) return "bg-green-200 text-green-600 font-medium hover:bg-green-400 hover:text-white";
    return "bg-gray-200 text-gray-600 hover:bg-gray-400 hover:text-white";
  }

  function seleccionarAsignatura(asignatura: any){    
    if(asignatura == null) return;
    if(asignaturasCursadas.find(ac => (ac.codigoAsignatura == asignatura.codigo && (ac.idEstado == 3 || ac.idEstado == 1)))) return;
    
    setAsignaturaSeleccionada(asignatura);
    setShowModal(true);
  }

  function inscribirAsignatura(asignatura: any){
    if(asignatura == null) return;    
    if(codigosAsignaturasInscritas.includes(asignatura.codigo)) {
      alert("La asignatura ya está inscrita.");
      setShowModal(false);
      return;
    };
    
    if(codigosAsignaturasInscritas.length >= asignaturasNivel.length) {
      alert(`Usted puede inscribir un máximo de ${asignaturasNivel.length} asignaturas.`);
      return;
    }    

    const reprobaciones:any = [];
    asignatura.codigosPrerrequisitos.forEach((codigoRequisito: {codigoRequisito: number}) => {
      const asignaturaCursada = asignaturasCursadas.find(ac => ac.codigoAsignatura == codigoRequisito);      
      if(asignaturaCursada == null) return;
      if(asignaturaCursada.idEstado != 3) {
        reprobaciones.push(asignaturaCursada.codigoAsignatura);
      }      
    })

    if(reprobaciones.length > 0) {
      alert(`No puedes inscribir la asignatura ${asignatura.nombre} porque no has aprobado los requisitos ${reprobaciones.map(r => r.toString())}`);
      return;
    }

    const topes:any = [];
    asignaturasInscritas.forEach(asignaturaInscrita => {
      if(asignaturaInscrita.horarios.some((horario: string) => asignatura.horarios.includes(horario))) {
        topes.push(asignaturaInscrita.codigo);        
      }
    });

    if(topes.length > 0) {
      alert(`No puedes inscribir la asignatura ${asignatura.nombre} porque hace tope con las asignaturas ${topes.map(t => t.toString())}`);
      return;
    }

    if(asignatura.inscritos >= asignatura.cupos) {
      alert(`No puedes inscribir la asignatura ${asignatura.nombre} porque no hay cupos disponibles ${asignatura.inscritos}/${asignatura.cupos}`);
      return;
    }

    setCodigosAsignaturasInscritas([...codigosAsignaturasInscritas, asignatura.codigo]);
    setAsignaturasInscritas([...asignaturasInscritas, asignatura]);
    handleAsignaturasInscritas([...asignaturasInscritas, asignatura]);
    setShowModal(false);
    alert(`Asignatura ${asignatura.nombre} inscrita correctamente`)
  }

  function desinscribirAsignatura(asignatura: any){ 
    const nuevasAsignaturas = asignaturasInscritas.filter(a => a.codigo != asignatura.codigo);
    const nuevosCodigosAsignaturas = codigosAsignaturasInscritas.filter(c => c != asignatura.codigo);
    setAsignaturasInscritas(nuevasAsignaturas);
    setCodigosAsignaturasInscritas(nuevosCodigosAsignaturas);
    setShowModal(false);
    alert("Asignatura desinscrita correctamente.");
  }

  const highlightPrerrequisitos = (asignatura: any) => {    
    setHighlighted(asignatura.codigosPrerrequisitos);
  }

  const unhighlightPrerrequisitos = (asignatura: any) => {
    setHighlighted([]);
  }

  async function finalizarProceso(){
    if(codigosAsignaturasInscritas.length < 3) {
      alert("Debes inscribir al menos 3 asignaturas.");
      return;
    }

    const response = await fetch(`http://localhost:8081/api/estudiantes/${rut}/asignaturas`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(codigosAsignaturasInscritas)
    });

    if(response.status < 200 || response.status > 299) {
      alert("Error al inscribir asignaturas");
      return;
    }

    const rawResponse = await response.json();

    console.log("asignaturas inscritas", rawResponse);

    alert("Asignaturas inscritas, proceso finalizado correctamente");
    location.reload();
  }

  return(
    <div>
      <div className="flex flex-col gap-3">        
        <div className="flex justify-center gap-3 bg-orange-600 text-white">
          {
            niveles.map(nivel => (
              <div className="w-28 text-center" key={nivel}>
                Nivel {nivel}
              </div>
            ))
          }
        </div>
          <div className="flex justify-center gap-3">
            {
              niveles.map(nivel => (
                <div className="w-28 text-center flex flex-col gap-3" key={nivel}>
                    {
                      asignaturas.filter(asignatura => asignatura.nivel === nivel).map(asignatura => (                                              
                          <div key={asignatura.codigo} 
                            className={`${obtenerEstiloAsignatura(asignatura.codigo)} flex justify-center items-center w-28 h-28 text-sm px-2 font-medium cursor-pointer`}
                            onMouseEnter={() => highlightPrerrequisitos(asignatura)}
                            onMouseLeave={() => unhighlightPrerrequisitos(asignatura)}
                            onClick={() => seleccionarAsignatura(asignatura)}
                          >
                            {asignatura.nombre}
                          </div>                                            
                      ))
                    }
                </div>
              ))
            }
        </div>
        <div className="w-full flex justify-center mt-3">
          <Button onClick={finalizarProceso}>Finalizar proceso</Button>
        </div>
      </div>
      {
        showModal && (
          <div className="fixed left-0 top-0 w-screen h-screen backdrop-blur-sm">
            <div className="flex justify-center fixed left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 drop-shadow-md bg-white z-10 rounded-md py-3 w-[1350px]">
              <div className="flex flex-col items-center gap-3">                             
                  <div className="text-center">                    
                    <h3 className="font-semibold text-2xl text-amber-600">{asignaturaSeleccionada?.nombre} <b className="font-medium text-xl text-slate-900">({asignaturaSeleccionada?.codigo})</b></h3>                                                               
                  </div>                                    
                  <div className="w-full flex justify-start">
                    <b className="font-bold">{asignaturaSeleccionada?.inscritos}</b> &nbsp; de &nbsp; <b>{asignaturaSeleccionada?.cupos}</b> &nbsp; alumnos inscritos
                  </div>
                  <InscribirAsignaturaForm asignatura={asignaturaSeleccionada} asignaturasInscritas={asignaturasInscritas}/>
                  <div className="w-full flex justify-end gap-3">
                    <Button variant="outline" onClick={() => setShowModal(false)}>Volver</Button>
                    <Button variant="destructive" onClick={() => desinscribirAsignatura(asignaturaSeleccionada)}>Desinscribir</Button>
                    <Button onClick={() => inscribirAsignatura(asignaturaSeleccionada)}>Inscribir</Button>
                  </div>
                </div>              
            </div>
          </div>
        )
      }
    </div>
  )
}