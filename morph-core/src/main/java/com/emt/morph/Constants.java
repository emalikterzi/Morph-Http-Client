package com.emt.morph;


import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

public interface Constants {


   Class<Path> PATH_CLASS = Path.class;

   Class<DefaultValue> DEFAULT_VALUE_CLASS = DefaultValue.class;

   Class<QueryParam> QUERY_PARAM_CLASS = QueryParam.class;

   Class<PathParam> PATH_PARAM_CLASS = PathParam.class;

   Class<Consumes> CONSUMES_CLASS = Consumes.class;

   Class<Produces> PRODUCES_CLASS = Produces.class;


}
