"use client"

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
import { Input } from "@/components/ui/input"
import { useState } from "react"
import HeaderEstudiante from "./header-estudiante"
import MallaCurricular from "./malla-curricular"
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card"
import { CircleIcon } from "lucide-react"

const FormSchema = z.object({
  rut: z.string().min(2, {
    message: "El rut debe tener mínimo 2 caracteres.",
  }),
})

export function SeleccionarEstudianteForm() {
  const [estudiante, setEstudiante] = useState(null);
  const [asignaturasInscritas, setAsignaturasInscritas] = useState([]);
  
  const form = useForm<z.infer<typeof FormSchema>>({
    resolver: zodResolver(FormSchema),
    defaultValues: {
      rut: "",
    },
  })

  async function onSubmit(data: z.infer<typeof FormSchema>) {
    const response = await fetch(`http://localhost:8081/api/estudiantes/${data.rut}`);
    if(response.status < 200 || response.status > 299) {
      alert("No se encontró el estudiante");
      return;
    }
    const rawResponse = await response.json();  
    console.log(rawResponse);
    if(!rawResponse.proceso) {
      alert("El estudiante ya realizó el proceso de inscripción de asignaturas.");
      return;
    }    
    if(rawResponse.asignaturasCursadas.filter(ac => ac.idEstado == 2 && (ac.reprobaciones >= 2 && (rawResponse.asignaturas.find(a => a.codigo == ac.codigoAsignatura).nivel == 1) || ac.reprobaciones >= 3)).length > 0) {
      alert("Usted no puede inscribir asignaturas debido a que tiene más de 2 reprobaciones en asignaturas de primer nivel o más de 3 reprobaciones en asignaturas de segundo nivel o superior. Consulte a un administrador.");
      return;
    }
    setEstudiante(rawResponse);
    setAsignaturasInscritas(rawResponse?.asignaturas.filter(a => rawResponse.asignaturasCursadas.find(ac => ac.codigoAsignatura == a.codigo && ac.idEstado == 1)));    
  }

  async function handleAsignaturasInscritas(asignaturas: []) {        
    setAsignaturasInscritas(asignaturas);
  }

  return (
    <Card className="px-5 py-5">
      <div className="flex justify-between gap-10 pb-5">      
        <div>                  
          <h1 className="text-2xl font-semibold leading-none tracking-tight mb-3">Inscribir asignaturas</h1>                              
          <div className="flex flex-col gap-5">
            <div className="flex justify-between items-start">        
              <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="flex gap-5 items-end grow">
                  <FormField
                    control={form.control}
                    name="rut"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>RUT</FormLabel>
                        <FormControl>
                          <Input placeholder="Ej: 12345678-9" {...field} />
                        </FormControl>           
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <Button type="submit">Ver Asignaturas</Button>
                </form>
              </Form>                              
            </div>                 
          </div>          
        </div>          
        <Card className="flex flex-col items-start justify-start grow h-min-[200px] h-full py-2 px-3 gap-1">            
          <h2 className="text-xl font-semibold text-amber-600 leading-none tracking-tight">Asignaturas inscritas</h2>                      
          <div className="h-full">            
            {asignaturasInscritas.map(asignatura => (
              <div key={asignatura.codigo} className="flex items-center">
                <CircleIcon className="w-3 h-3 inline-block mr-2 text-amber-600" />
                <div>
                  <span className="text-gray-700 font-medium">{asignatura.nombre}</span> ({asignatura.codigo}): {asignatura.horarios.map(h => h)}
                </div>
              </div>            
            ))}    
          </div>          
        </Card>
      </div>
      {estudiante &&
        <>
          <HeaderEstudiante estudiante={estudiante} />
          <MallaCurricular 
            asignaturas={estudiante.asignaturas} 
            asignaturasCursadas={estudiante.asignaturasCursadas} 
            asignaturasInscritas={asignaturasInscritas}            
            rut={estudiante.rut} 
            nivel={estudiante.nivel}             
            setAsignaturasInscritas={setAsignaturasInscritas}
            handleAsignaturasInscritas={handleAsignaturasInscritas}/>
        </>
      }   
    </Card>      
  )
}
