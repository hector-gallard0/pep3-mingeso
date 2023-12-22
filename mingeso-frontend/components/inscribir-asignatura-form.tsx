"use client";

import { horarios } from '@/app/utils/horarios'
import { useMemo, useState } from 'react';

export default function InscribirAsignaturaForm({asignatura, asignaturasInscritas}: {asignatura: any, asignaturasInscritas: []}) {  
  const [totalAsignaturas, setTotalAsignaturas] = useState([...asignaturasInscritas, asignatura]);
  const [asignaturasHorarios, setAsignaturasHorarios] = useState(new Map());

  useMemo(() => {
    const nuevasAsignaturasHorarios = asignaturasHorarios;

    ['L', 'M', 'W', 'J', 'V', 'S'].map((dia) => 
      [1, 2, 3, 4, 5, 6, 7, 8, 9].map((modulo) => (
        nuevasAsignaturasHorarios.set(`${dia}${modulo}`, totalAsignaturas.filter(as => as.horarios.find(h => h == `${dia}${modulo}`)))
      ))
    )
    
    setAsignaturasHorarios(nuevasAsignaturasHorarios);    
  }, [totalAsignaturas]);

  return(       
    <table className="border">
      <thead>
        <tr>
          <th>Módulo</th>
          <th>Lunes</th>
          <th>Martes</th>
          <th>Miércoles</th>
          <th>Jueves</th>
          <th>Viernes</th>
          <th>Sábado</th>                  
        </tr>
      </thead>
      <tbody>
        {
          asignaturasHorarios.size > 0 &&
          (horarios.map(horario => (
            <tr key={horario.modulo}>
              <td className="px-4 w-fit text-center border">({horario.modulo}) {horario.hora}</td>
              {['L', 'M', 'W', 'J', 'V', 'S'].map((dia, index) => (
                <td key={index} className="w-48 border h-[50px] p-2">                                                  
                  <div className="flex flex-col gap-1">
                    {asignaturasHorarios.get(`${dia}${horario.modulo}`).length > 1 && (<span className='text-red-500'>*Tope de horario</span>)}
                    {asignaturasHorarios.get(`${dia}${horario.modulo}`).map((as) => (                                              
                      <div>                        
                        <span className="text-gray-700 text-base font-medium">{as.nombre}</span> ({as.codigo})
                      </div>                      
                    ))}    
                  </div>
                </td>
              ))}
            </tr>
          )))
        }
      </tbody>
    </table>
  )
}