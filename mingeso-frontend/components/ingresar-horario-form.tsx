"use client";
import { horarios } from '@/app/utils/horarios'
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import * as z from "zod"

import { Button } from "@/components/ui/button"
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { useState } from "react";
import { CheckIcon } from "lucide-react";

const FormSchema = z.object({
  codigoCarrera: z
    .string({
      required_error: "Por favor, selecciona una carrera.",
    }),
  codigoAsignatura: z
    .string({
      required_error: "Por favor, selecciona una asignatura.",
    }),
})

const cache:any = {};

const fetchData = async (codigoCarrera: any) => {
  const response = await fetch(`http://localhost:8080/api/asignaturas/${codigoCarrera}`);
  const asignaturas = await response.json();
  return asignaturas;
}

const memoizedAsignaturas = async (codigoCarrera: any) => {
  if (cache[codigoCarrera]) {
    console.log("cache hit");
    return cache[codigoCarrera];
  }
  console.log("cache miss");
  const data = fetchData(codigoCarrera);
  cache[codigoCarrera] = data;
  return data;
}

export default function IngresarHorarioForm({carreras}: {carreras: any[]}) {
  const [asignaturas, setAsignaturas] = useState([]);
  const [asignaturaSeleccionada, setAsignaturaSeleccionada] = useState(null);
  const [horariosSeleccionados, setHorariosSeleccionados] = useState(new Map());

  const form = useForm<z.infer<typeof FormSchema>>({
    resolver: zodResolver(FormSchema),
  })

  function onChangeCarrera(codigoCarrera: any, field: any){
    field.onChange(codigoCarrera); 

    setAsignaturas([]);
    setHorariosSeleccionados(new Map());

    (async function() {
      const asignaturas = await memoizedAsignaturas(codigoCarrera);
      setAsignaturas(asignaturas);
      console.log(asignaturas);
    })();
  }

  function onChangeAsignatura(codigoAsignatura: any, field: any){
    field.onChange(codigoAsignatura); 
    
    const asignaturaSeleccionadaAux = asignaturas.find(asignatura => asignatura.codigo == codigoAsignatura);
    setAsignaturaSeleccionada(asignaturaSeleccionadaAux);    
  
    const nuevosHorariosSeleccionados = new Map();
    asignaturaSeleccionadaAux?.horarios.forEach(horario => {
      nuevosHorariosSeleccionados.set(horario, true);
    })
    
    setHorariosSeleccionados(nuevosHorariosSeleccionados);
  }

  function onClickHorario(horario: string){
    if(asignaturaSeleccionada == null) {
      alert("Debes seleccionar una carrera y una asignatura primero.");
      return;
    };

    const nuevosHorariosSeleccionados = new Map(horariosSeleccionados);
    horariosSeleccionados.has(horario) ? nuevosHorariosSeleccionados.delete(horario) : nuevosHorariosSeleccionados.set(horario, true);
    asignaturaSeleccionada.horarios = Array.from(nuevosHorariosSeleccionados.keys());
    setHorariosSeleccionados(nuevosHorariosSeleccionados);
    setAsignaturaSeleccionada(asignaturaSeleccionada);

    const indexAsignaturaSeleccionada = asignaturas.findIndex(asignatura => asignatura.codigo == asignaturaSeleccionada.codigo);
    const nuevasAsignaturas = [...asignaturas];
    nuevasAsignaturas[indexAsignaturaSeleccionada] = asignaturaSeleccionada;
    setAsignaturas(nuevasAsignaturas);    
  }

  async function onSubmit(data: z.infer<typeof FormSchema>) {
    const body = asignaturas.map(asignatura => (
      {
        codigoAsignatura: asignatura.codigo,
        codigosHorarios: asignatura.horarios
      }
    ))

    const response = await fetch('http://localhost:8080/api/asignaturas/horarios', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(body)
    });

    const rawResponse = await response.json();

    if(rawResponse) {
      alert("Horarios ingresados correctamente.");
    }else{
      alert("Error al ingresar los horarios.");
    }

  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-8 relative">
        <div className="flex gap-10">
          <FormField            
            control={form.control}
            name="codigoCarrera"
            render={({ field }) => (
              <FormItem className="grow">
                <FormLabel>Seleccionar carrera</FormLabel>
                <Select onValueChange={(codigoCarrera) => onChangeCarrera(codigoCarrera, field)} defaultValue={field.value}>
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Seleccionar carrera" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    {
                      carreras.map(carrera => (
                        <SelectItem key={carrera.codigo} value={carrera.codigo.toString()}>{carrera.nombre}</SelectItem>
                        ))
                      }                  
                  </SelectContent>
                </Select>              
                <FormMessage />
              </FormItem>
            )}
            />

          <FormField
            control={form.control}
            name="codigoAsignatura"
            render={({ field }) => (
              <FormItem className="grow">
                <FormLabel>Seleccionar asignatura</FormLabel>
                <Select onValueChange={(codigoCarrera) => onChangeAsignatura(codigoCarrera, field)} defaultValue={field.value}>
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Seleccionar asignatura" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    {
                      asignaturas.map(asignatura => (
                        <SelectItem key={asignatura.codigo} value={asignatura.codigo.toString()}>{asignatura.nombre}</SelectItem>
                        ))
                      }                  
                  </SelectContent>
                </Select>              
                <FormMessage />
              </FormItem>
            )}
            />
          </div>          

          <div>
            <table className="border w-full">
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
                  horarios.map(horario => (
                    <tr key={horario.modulo}>
                      <td className="px-4 w-fit text-center border">({horario.modulo}) {horario.hora}</td>
                      {['L', 'M', 'W', 'J', 'V', 'S'].map((dia) => (
                        <td key={dia} className="w-48 h-16 border cursor-pointer hover:bg-green-100" onClick={() => onClickHorario(`${dia}${horario.modulo}`)}>
                          <span className="flex justify-center">
                            {horariosSeleccionados.has(`${dia}${horario.modulo}`) && (<>{`${dia}${horario.modulo}`} <CheckIcon className="mx-1 text-green-500" /></>)} 
                          </span>
                        </td>
                      ))}
                    </tr>
                  ))
                }
              </tbody>
            </table>
          </div>

          <div className="flex justify-end">
            <Button className="w-fit" type="submit">Guardar cambios</Button>        
          </div>
      </form>    
    </Form>
  )
}
