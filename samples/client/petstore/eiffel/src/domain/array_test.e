note
 description:"[
		Swagger Petstore
 		This spec is mainly for testing Petstore server and contains fake endpoints, models. Please do not use this for any other purpose. Special characters: \" \\
  		OpenAPI spec version: 1.0.0
 	    Contact: apiteam@swagger.io

  	NOTE: This class is auto generated by the swagger code generator program.

 		 Do not edit the class manually.
 	]"
	date: "$Date$"
	revision: "$Revision$"
	EIS:"Eiffel swagger codegen", "src=https://github.com/swagger-api/swagger-codegen.git", "protocol=uri"

class ARRAY_TEST 

inherit

  ANY
      redefine
          out 
      end


feature --Access

    array_of_string: detachable LIST [STRING_32] 
      
    array_array_of_integer: detachable LIST [LIST [INTEGER_64]] 
      
    array_array_of_model: detachable LIST [LIST [READ_ONLY_FIRST]] 
      

feature -- Change Element  
 
    set_array_of_string (a_name: like array_of_string)
        -- Set 'array_of_string' with 'a_name'.
      do
        array_of_string := a_name
      ensure
        array_of_string_set: array_of_string = a_name		
      end

    set_array_array_of_integer (a_name: like array_array_of_integer)
        -- Set 'array_array_of_integer' with 'a_name'.
      do
        array_array_of_integer := a_name
      ensure
        array_array_of_integer_set: array_array_of_integer = a_name		
      end

    set_array_array_of_model (a_name: like array_array_of_model)
        -- Set 'array_array_of_model' with 'a_name'.
      do
        array_array_of_model := a_name
      ensure
        array_array_of_model_set: array_array_of_model = a_name		
      end


 feature -- Status Report

    out: STRING
          -- <Precursor>
      do
        create Result.make_empty
        Result.append("%Nclass ARRAY_TEST%N")
        if attached array_of_string as l_array_of_string then
          across l_array_of_string as ic loop
            Result.append ("%N")
            Result.append (ic.item.out)
            Result.append ("%N")
          end
        end 
        if attached array_array_of_integer as l_array_array_of_integer then
          across l_array_array_of_integer as ic loop
            Result.append ("%N")
            Result.append (ic.item.out)
            Result.append ("%N")
          end
        end 
        if attached array_array_of_model as l_array_array_of_model then
          across l_array_array_of_model as ic loop
            Result.append ("%N")
            Result.append (ic.item.out)
            Result.append ("%N")
          end
        end 
      end
end
