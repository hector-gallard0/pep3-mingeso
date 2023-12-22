"use client";

export default function HeaderEstudiante({estudiante}: {estudiante: any}){
  return(
    <div className="flex flex-col">                                      
      <span className="text-xl text-amber-600 font-medium">
        {estudiante.nombre} {estudiante.apellido} - RUT: {estudiante.rut}
      </span>
      <span className="text-xl font-semibold tracking-wider">
        {estudiante.carrera?.nombre}                
      </span>
    </div>
  )
}