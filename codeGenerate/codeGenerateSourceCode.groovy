fileName = ""
author = ""
date = ""
version = ""
brief = ""
copyright = ""
prefix = ""

includeStr =  ""
defineStr =  ""
structStr = ""
enumStr = ""
unionStr = ""
typedefStr = ""

staticFunctionStr = ""
globalFunctionStr = ""
staticPrototypeFunctionStr = ""
prototypeFunctionStr = ""
externPrototypeFunctionStr = ""

staticVariableStr = ""
globalVariableStr = ""
constantVariableStr = ""
externGlobalVariableStr = ""

functionPrefix = ""
constantVariablePrefix = ""
globalVariablePrefix = ""

def file(node)
{
    if( node.text() != " ")
    {
      fileName = node.name.text()
      author = node.author.text()
      date =  node.date.text()
      version = node.version.text()
      brief = node.brief.text()
      copyright = node.copyright.text()
      prefix = node.prefix.text()
      
      if(prefix == "true")
      {
         functionPrefix = fileName + "_"
         globalVariablePrefix = "g_"
         constantVariablePrefix = "c_"
      }
      
      println fileName
      println author
      println date
      println version
      println brief
      println copyright
      println functionPrefix
      println globalVariablePrefix
      println constantVariablePrefix
    }
    else
    {
      println "File format error"
      fileName = "temp"
    }
}


def include(node)
{
    if( node.text() != "")
    {
      index = 0
      node.size().times
      {
        if(node[index].@form == null)
        {
           includeStr += '#include <' + node[index].text().replaceAll("\n      ", "") + '>\n'
        }
        else if (node[index].@form == 'quote')
        {
          includeStr += '#include "' + node[index].text().replaceAll("\n      ", "") + '"\n'
        }
        else if (node[index].@form == 'angle')
        {
          includeStr += '#include <' + node[index].text().replaceAll("\n      ", "") + '>\n'
        }
        else
        {
            println "Include format error at ${index+1} line definition" 
        }
        index++
      }
       
      println includeStr
    }   
}


def define(node)
{
    def controlStr = ""
    if( node.text() != "")
    {
      index =0
      node.size().times
      {
       def paramStr = []
       def valueStr = []
        
       paramStr = param(node,index)
       valueStr = value(node,index)
       
       if(valueStr.size() == 0  && paramStr.size() == 0)
       {
          defineStr += '#define ' + node[index].text() + '\n'
       }
       else if(valueStr.size() == 1  && paramStr.size() == 1)
       {
          defineStr += '#define ' + paramStr[0] + ' ('   + valueStr[0] + ') ' + '\n'
          controlStr = comment(node[index].comment)
          if(controlStr != "")
          {
              defineStr += controlStr 
          }
       }
       else
       {
          println "Define format error at ${index+1} line definition" 
       }
       index++
      }
    }
    
    println defineStr
}

def param(node , index)
{
    def paramStr = []
    if(node[index].param.text() != "")
    {
       index1 = 0
       node[index].param.size().times
       {
           paramStr.add(node[index].param[index1].text())
           index1++
        }
    }
    return paramStr
}

def value(node , index)
{
    def valueStr = []
    if(node[index].value.text() != "")
    {
       index1 = 0
       node[index].value.size().times
       {
           valueStr.add(node[index].value[index1].text())
           index1++
        }
    }
    return valueStr
}


def variable(node)
{
    def controlStr = ""
    if( node.text() != "")
    {
      index =0
      node.size().times
      {
       def paramStr = []
       def typeStr = []
       def sizesofStr = []
        
       paramStr = param(node,index)
       typeStr = type(node,index)
       sizesofStr = sizesof(node,index)
       
       if(node[index].@form == 'static')
       {
           if( paramStr.size() == 0 || typeStr.size() ==0 )
           {
              println "Define format error at ${index+1} param definition" 
           }
           else if( paramStr.size() == typeStr.size() )
           {
              if( sizesofStr.size() == 0 )
              {
                 index1=0
                 paramStr.size().times
                 {
                    staticVariableStr += 'static ' + typeStr[index1] + ' ' + paramStr[index1] + ';' + '\n'
                    index1++
                 }
                 controlStr = comment(node[index].comment)
                 if(controlStr != "")
                 {
                     staticVariableStr += controlStr 
                 }
              }
              else if( paramStr.size() == sizesofStr.size() )
              {
                 index1=0
                 paramStr.size().times
                 {
                    staticVariableStr += 'static ' + typeStr[index1] + ' ' + paramStr[index1] + '[' + sizesofStr[index1] + ']' + '= ' + '\n' + '{' + '\n\n' + '}' + ';' + '\n'
                    index1++
                 }
                 controlStr = comment(node[index].comment)
                 if(controlStr != "")
                 {
                     staticVariableStr += controlStr 
                 }
              }
              else
              {
                  println "Define format error at ${index+1} param definition" 
              }
           }
       }
       else if(node[index].@form == 'global')
       { 
           if( paramStr.size() == 0 || typeStr.size() ==0 )
           {
              println "Define format error at ${index+1} param definition" 
           }
           else if( paramStr.size() == typeStr.size() )
           {
              if( sizesofStr.size() == 0 )
              {
                 index1=0
                 paramStr.size().times
                 {
                    globalVariableStr += typeStr[index1] + ' ' + globalVariablePrefix + paramStr[index1] + ';' + '\n'
                    externGlobalVariableStr += 'extern ' + typeStr[index1] + ' ' + globalVariablePrefix  + paramStr[index1] + ';' + '\n'
                    index1++
                 }
                 controlStr = comment(node[index].comment)
                 if(controlStr != "")
                 {
                     globalVariableStr += controlStr 
                 }
              }
              else if( paramStr.size() == sizesofStr.size() )
              {
                 index1=0
                 paramStr.size().times
                 {
                    globalVariableStr += typeStr[index1] + ' ' + globalVariablePrefix + paramStr[index1] + '[' + sizesofStr[index1] + ']' + '= ' + '\n' + '{' + '\n\n' + '}' + ';' + '\n'
                    externGlobalVariableStr += 'extern ' + typeStr[index1] + ' ' + globalVariablePrefix + paramStr[index1] + '[]' + ';' + '\n'
                    index1++
                 }
                 controlStr = comment(node[index].comment)
                 if(controlStr != "")
                 {
                     globalVariableStr += controlStr 
                 }
              }
              else
              {
                  println "Define format error at ${index+1} param definition" 
              }
           }
       }
       else if(node[index].@form == 'constant')
       {
           if( paramStr.size() == 0 || typeStr.size() ==0 )
           {
              println "Define format error at ${index+1} param definition" 
           }
           else if( paramStr.size() == typeStr.size() )
           {
              if( sizesofStr.size() == 0 )
              {
                 index1=0
                 paramStr.size().times
                 {
                    constantVariableStr += 'const ' + typeStr[index1] + ' ' + constantVariablePrefix  + paramStr[index1] + ';' + '\n'
                    index1++
                 }
                 controlStr = comment(node[index].comment)
                 if(controlStr != "")
                 {
                     constantVariableStr += controlStr 
                 }
              }
              else if( paramStr.size() == sizesofStr.size() )
              {
                 index1=0
                 paramStr.size().times
                 {
                    constantVariableStr += 'const ' + typeStr[index1] + ' '  + constantVariablePrefix + paramStr[index1] + '[' + sizesofStr[index1] + ']' + '= ' + '\n' + '{' + '\n\n' + '}' + ';' + '\n'
                    index1++
                 }
                 controlStr = comment(node[index].comment)
                 if(controlStr != "")
                 {
                     constantVariableStr += controlStr 
                 }
              }
              else
              {
                  println "Define format error at ${index+1} param definition" 
              }
           }
       }
       else if(node[index].@form == 'constant global')
       {
           if( paramStr.size() == 0 || typeStr.size() ==0 )
           {
              println "Define format error at ${index+1} param definition" 
           }
           else if( paramStr.size() == typeStr.size() )
           {
              if( sizesofStr.size() == 0 )
              {
                 index1=0
                 paramStr.size().times
                 {
                    constantVariableStr += 'const ' + typeStr[index1] + ' ' + constantVariablePrefix + paramStr[index1] + ';' + '\n'
                    externGlobalVariableStr += 'extern const ' + typeStr[index1] + '  '+ constantVariablePrefix + paramStr[index1] + ';' + '\n'
                    index1++
                 }
                 controlStr = comment(node[index].comment)
                 if(controlStr != "")
                 {
                     constantVariableStr += controlStr 
                 }
              }
              else if( paramStr.size() == sizesofStr.size() )
              {
                 index1=0
                 paramStr.size().times
                 {
                    constantVariableStr += 'const ' + typeStr[index1] + ' ' + constantVariablePrefix + paramStr[index1] + '[' + sizesofStr[index1] + ']' + '= ' + '\n' + '{' + '\n\n' + '}' + ';' + '\n'
                    externGlobalVariableStr += 'extern const ' + typeStr[index1] + ' ' + constantVariablePrefix + paramStr[index1] + '[]' + ';' + '\n'
                    index1++
                 }
                 controlStr = comment(node[index].comment)
                 if(controlStr != "")
                 {
                     constantVariableStr += controlStr 
                 }
              }
              else
              {
                  println "Define format error at ${index+1} param definition" 
              }
           }
       }
       else
       {
          println "Define format error at ${index+1} form definition" 
       }
       index++
      }
    }
    
    println staticVariableStr
    println globalVariableStr
    println constantVariableStr
    println externGlobalVariableStr
}

def sizesof(node , index)
{
    def sizesofStr = []
    
    if( node[index].sizeof.text() != "")
    {
       index1= 0
       node[index].sizeof.size().times
       {
         sizesofStr.add(node[index].sizeof[index1].text())
         index1++
       }
    }
    
    return sizesofStr
}

def struct(node)
{
    def controlStr = ""
    if( node.text() != "")
    {
      index =0
      node.size().times
      {
        def paramStr = []
        def memberStr = []
        def typeStr = []
        
        paramStr = param(node,index)
        memberStr = member(node,index)
        typeStr = type(node,index)
           
     
       if(paramStr.size() == 0)
       {
          println "struct format error at ${index+1} param definition" 
       }
       else if(memberStr.size() == 0 && typeStr.size() == 0)
       {
           structStr += 'struct ' + paramStr[0] + ';' +  '\n'
           controlStr = comment(node[index].comment)
           if(controlStr != "")
           {
               structStr += controlStr 
           } 
       }
       else if(memberStr.size() == typeStr.size())
       {
            structStr += 'struct ' + '\n' + "{" +  '\n'
            index1=0
            memberStr.size().times
            {
               structStr += '  ' + typeStr[index1] + ' ' + memberStr[index1] + ';'  +  '\n'
               index1++
            }
            structStr += controlCNodeData(node[index]) + "}" + paramStr[0] + ';' +  '\n'
            controlStr =  comment(node[index].comment)
            if(controlStr != "")
            {
               structStr += controlStr 
            } 
       } 
       else
       {
          println "struct format error at ${index+1} . member definition" 
       }
       index++
      }
    } 
    
     println structStr
}

def member(node , index)
{
    def memberStr = []
    if(node[index].member.text() != "")
    {
       index1 = 0
       node[index].member.size().times
       {
           memberStr.add(node[index].member[index1].text())
           index1++
       }
    }
    return memberStr
}

def type(node , index)
{
    def typeStr = []
    
    if(node[index].type.text() != "")
    {
       index1 = 0
       node[index].type.size().times
       {
           typeStr.add(node[index].type[index1].text())
           index1++
       }
    }
    return typeStr
}


def union(node)
{ 
    def controlStr = ""
    if( node.text() != "")
    {
      index =0
      node.size().times
      {
        def paramStr = []
        def memberStr = []
        def typeStr = []
        
        paramStr = param(node,index)
        memberStr = member(node,index)
        typeStr = type(node,index)
           
     
       if(paramStr.size() == 0)
       {
          println "union format error at ${index+1} param definition" 
       }
       else if(memberStr.size() == 0 && typeStr.size() == 0)
       {
           unionStr += 'union ' + paramStr[0] + ';' +  '\n'
           controlStr =  comment(node[index].comment)
           if(controlStr != "")
           {
               unionStr += controlStr 
           } 
       }
       else if(memberStr.size() == typeStr.size())
       {
            unionStr += 'union ' + '\n' + "{" +  '\n'
            index1=0
            memberStr.size().times
            {
               unionStr += '  ' + typeStr[index1] + ' ' + memberStr[index1] + ';'  +  '\n'
               index1++
            }
            unionStr += controlCNodeData(node[index]) + "}" + paramStr[0] + ';' +  '\n'
            controlStr = comment(node[index].comment)
            if(controlStr != "")
            {
               unionStr += controlStr 
            } 
       } 
       else
       {
          println "union format error at ${index+1} . member definition" 
       }
       index++
      }
    } 
    
    println unionStr
   
}

def enumerate(node)
{
    def controlStr = ""
    if( node.text() != "")
    {
      index =0
      node.size().times
      {
        def paramStr = []
        def constantStr = []
        def valueStr = []
        
        paramStr = param(node,index)
        constantStr = constant(node,index)
        valueStr = value(node,index)
           
       if(paramStr.size() == 0)
       {
          println "enum format error at ${index+1} param definition" 
       }
       else if(constantStr.size() == 0)
       {
           enumStr += 'enum ' + paramStr[0] + ';' +  '\n'
           controlStr = comment(node[index].comment)
           if(controlStr != "")
           {
               enumStr += controlStr 
           } 
       }
       else if(constantStr.size() == valueStr.size())
       {
            enumStr += 'enum ' + '\n' + "{" +  '\n'
            index1=0
            constantStr.size().times
            {
               if(valueStr[index1] != 'NULL')
               {
                   enumStr += '  ' + constantStr[index1] + ' =' + valueStr[index1] + ','  +  '\n'
               }
               else
               {
                   enumStr += '  ' + constantStr[index1]  + ','  +  '\n'
               }
               index1++
            }
            enumStr += "}" + paramStr[0] + ';' +  '\n'
            controlStr = comment(node[index].comment)
            if(controlStr != "")
            {
               enumStr += controlStr 
            } 
       } 
       else
       {
          println "enum format error at ${index+1} . member definition" 
       }
       index++
      }
    } 
    println enumStr
}

def constant(node , index)
{
    def constantStr = []
    if(node[index].constant.text() != "")
    {
       index1 = 0
       node[index].constant.size().times
       {
           constantStr.add(node[index].constant[index1].text())
           index1++
        }
    }
    return constantStr
}

def typedef(node)
{
    def controlStr = ""
    if( node.text() != "")
    {
      index =0
      node.size().times
      {
        def paramStr = []
        def typeStr = []
        
        paramStr = param(node,index)
        typeStr = type(node,index)
     
       if(paramStr.size() == 0)
       {
          println "typedef format error at ${index+1} param definition" 
       }
       else if (paramStr.size() == 1 && typeStr.size() == 1)
       {
          controlStr = controlCNodeData(node[index].type[0]) 
          if(controlStr == "")
          {
              typedefStr += 'typedef ' + paramStr[0] + ' ' +  typeStr[0] + ';'  +  '\n'
          }
          else
          {
              typedefStr += 'typedef ' + controlStr  +  '\n'
          }
       }
       else if(paramStr.size() == 1)
       {
           typedefStr += 'typedef ' + paramStr[0] + ';'  +  '\n'
       }
       else
       {
         println "typedef format error at ${index+1} . member definition" 
       }
       index++
      }
    } 
     println typedefStr
}

def comment(node)
{
   def commnentStr = ""
   
    if( node.text() != "")
    {
        commnentStr += '/**\n *  ' + node.text().replaceAll("\n","\n *")  + '\n */' + '\n\n'
    }
   
   return commnentStr
}

def globalFunctionGenerate(node, index, paramStr, typeStr)
{
    globalFunctionStr += addDoxygenFormatFunction(node, index, paramStr, typeStr)
    
    if(node[index].name.size() == 0 || node[index].return.size() == 0)
    {
        println "function format error at ${index+1} . name and return definition" 
    }
    if(paramStr.size() == 0 && typeStr.size() == 0)
    {
       globalFunctionStr += node[index].return.text() + ' ' + functionPrefix + node[index].name.text() + '(void)' + "\n" + "{" +  "\n\n" + "}" + "\n\n"
    }
    else if(paramStr.size() == typeStr.size())
    {
       globalFunctionStr += node[index].return.text() + ' ' + functionPrefix + node[index].name.text() + "("
       
       index=0
       paramStr.size().times
       {
           if(index <  paramStr.size()-1)
           {
               globalFunctionStr +=  typeStr[index] + " " +  paramStr[index] + ", "
           }
           else
           {
               globalFunctionStr +=  typeStr[index] + " " +  paramStr[index] + ")"
           }
           index++
       }
       globalFunctionStr +=  "\n" + "{" +  "\n\n" + "}" + "\n\n"
    }
    else
    {
        println "function format error at ${index+1} . param definition" 
    }
}

def prototypeFunctionGenerate(node, index, paramStr, typeStr, staticControl, externControl)
{
    if(node[index].name.size() == 0 || node[index].return.size() == 0)
    {
        println "function format error at ${index+1} . name and return definition" 
    }
    if(paramStr.size() == 0 && typeStr.size() == 0)
    {
       if(staticControl)
       {
           staticPrototypeFunctionStr += 'static ' + node[index].return.text() + ' '  + functionPrefix + node[index].name.text() + '(void);' + "\n"  
       }
       else
       {
           if(externControl)
           {
              externPrototypeFunctionStr += 'extern ' +node[index].return.text() + ' ' + functionPrefix + node[index].name.text() + '(void);' + "\n"  
           }
           else
           {
              prototypeFunctionStr += node[index].return.text() + ' ' + functionPrefix + node[index].name.text() + '(void);' + "\n"  
           }
       }
    }
    else if(paramStr.size() == typeStr.size())
    {
       if(staticControl)
       {
           staticPrototypeFunctionStr += 'static '+ node[index].return.text() + ' ' + functionPrefix + node[index].name.text() + "("
       }
       else
       {
           if(externControl)
           {
               externPrototypeFunctionStr += 'extern ' + node[index].return.text() + ' ' + functionPrefix + node[index].name.text() + "("
           }
           else
           {
              prototypeFunctionStr += node[index].return.text() + ' ' + functionPrefix + node[index].name.text() + "("
           }
       }
       
       index=0
       paramStr.size().times
       {
           if(index <  paramStr.size()-1)
           {
               prototypeFunctionStr +=  typeStr[index] + " " +  paramStr[index] + ", "
           }
           else
           {
               prototypeFunctionStr +=  typeStr[index] + " " +  paramStr[index] + ")"
           }
           index++
       }
       prototypeFunctionStr +=  ";" + "\n" 
    }
    else
    {
        println "function format error at ${index+1} . param definition" 
    }
}

def staticFunctionGenerate(node, index, paramStr, typeStr)
{
    staticFunctionStr += addDoxygenFormatFunction(node, index, paramStr, typeStr)
    
    if(node[index].name.size() == 0 || node[index].return.size() == 0)
    {
        println "function format error at ${index+1} . name and return definition" 
    }
    if(paramStr.size() == 0 && typeStr.size() == 0)
    {
       staticFunctionStr += "static " + node[index].return.text() + ' ' + functionPrefix + node[index].name.text() + '(void)' + "\n" + "{" +  "\n\n" + "}" + "\n\n"
    }
    else if(paramStr.size() == typeStr.size())
    {
       staticFunctionStr += "static " + node[index].return.text() + ' ' + functionPrefix + node[index].name.text() + "("
       
       index=0
       paramStr.size().times
       {
           if(index <  paramStr.size()-1)
           {
               staticFunctionStr +=  typeStr[index] + " " +  paramStr[index] + ", "
           }
           else
           {
               staticFunctionStr +=  typeStr[index] + " " +  paramStr[index] + ")"
           }
           index++
       }
       staticFunctionStr +=  "\n" + "{" +  "\n\n" + "}" + "\n\n"
    }
    else
    {
        println "function format error at ${index+1} . param definition" 
    }
}

def function(node)
{
   if( node.text() != "")
    {
      index =0
      node.size().times
      {
        def paramStr = []
        def typeStr = []
        
        paramStr = param(node,index)
        typeStr = type(node,index)
        
        if(node[index].@form == null)
        {
           globalFunctionGenerate(node,index,paramStr,typeStr)
        }
        else if(node[index].@form == "global")
        {
            globalFunctionGenerate(node,index,paramStr,typeStr)
        }
        else if(node[index].@form == "prototype static")
        {
           prototypeFunctionGenerate(node,index,paramStr,typeStr,true,false)
        }
        else if(node[index].@form == "prototype")
        {
           prototypeFunctionGenerate(node,index,paramStr,typeStr,false,false)
        }
         else if(node[index].@form == "prototype extern")
        {
           prototypeFunctionGenerate(node,index,paramStr,typeStr,false,true)
        }
        else if(node[index].@form == "static")
        {
           staticFunctionGenerate(node,index,paramStr,typeStr)
        }
        else
        {
           println "function format error at ${index+1} . form definition" 
        }
       index++
      }
    } 
     println globalFunctionStr
     println prototypeFunctionStr
     println staticFunctionStr
     println externPrototypeFunctionStr
}

def structControl(node)
{
    def controlStr = ""
    def structStr = ""
    if( node.text() != "")
    {
    
       if(node.param.size() == 0)
       {
          println "struct format error at 1. param definition" 
       }
       else if(node.member.size() == 0 && node.type.size() == 0)
       {
           structStr += 'struct ' + node.param[0].text() + ';' +  '\n'
           controlStr = comment(node.comment)
           if(controlStr != "")
           {
               structStr += controlStr 
           }
       }
       else if(node.member.size() == node.type.size( ))
       {
          
            structStr += 'struct ' + '\n' + "{" +  '\n'
            index1=0
            node.member.size().times
            {
              
               structStr += '  ' + node.type[index1].text() + ' ' + node.member[index1].text() + ';'  +  '\n'
               index1++
            }
           
            structStr +=  controlCNodeData(node)  + "}" + node.param[0].text() + ';' +  '\n'
           
            
            controlStr = comment(node.comment)
            if(controlStr != "")
            {
               structStr += controlStr 
            }
            
       } 
       else
       {
          println "struct format error at 1 . member definition" 
       }
    } 
    return structStr
}

def unionControl(node)
{
    def controlStr = ""
    def unionStr = ""
    if( node.text() != "")
    {
       if(node.param.size() == 0)
       {
          println "union format error at 1. param definition" 
       }
       else if(node.member.size() == 0 && node.type.size() == 0)
       {
           unionStr += 'union ' + node.param[0].text() + ';' +  '\n'
           controlStr = comment(node.comment)
           if(controlStr != "")
           {
              unionStr += controlStr 
           }
       }
       else if(node.member.size() == node.type.size( ))
       {
            unionStr += 'union ' + '\n' + "{" +  '\n'
            index1=0
            node.member.size().times
            {
               unionStr += '  ' + node.type[index1].text() + ' ' + node.member[index1].text() + ';'  +  '\n'
               index1++
            }
            
            unionStr += controlCNodeData(node) + "}" + node.param[0].text() + ';' +  '\n'
            
            controlStr = comment(node.comment)
            if(controlStr != "")
            {
               unionStr += controlStr 
            }
       } 
       else
       {
          println "union format error at 1 . member definition" 
       }
    } 
    return unionStr
}

def enumerateControl(node)
{
    def controlStr = ""
    def enumStr = ""
    if( node.text() != "")
    {
       if(node.param.size() == 0)
       {
          println "enum format error at 1. param definition" 
       }
       else if(node.constant.size() == 0)
       {
           enumStr += 'enum ' + node.param[0].text() + ';' +  '\n'
           controlStr = comment(node.comment)
           if(controlStr != "")
           {
               enumStr += controlStr 
           }
       }
       else if(node.constant.size() == node.value.size())
       {
            enumStr += 'enum ' + '\n' + "{" +  '\n'
            index1=0
            node.constant.size().times
            {
               if(node.value[index1].text() != 'NULL')
               {
                   enumStr += '  ' + node.constant[index1].text() + ' =' + node.value[index1].text() + ','  +  '\n'
               }
               else
               {
                   enumStr += '  ' + node.constant[index1].text()  + ','  +  '\n'
               }
               index1++
            }
            enumStr += "}" + node.param[0].text() + ';' +  '\n'
            
            controlStr = comment(node.comment)
            if(controlStr != "")
            {
               enumStr += controlStr 
            }
       } 
       else
       {
           println "enum format error at 1 . member definition" 
       }
    } 
    return enumStr
}

def controlCNodeData(cNode)
{
   def cNodeData = ""
 
   index=0
   cNode.struct.size().times
   {
     cNodeData += structControl(cNode.struct[index])
     index++
   }
   index=0
   cNode.union.size().times
   {
     cNodeData += unionControl(cNode.union[index])
     index++
   }
   cNodeData += enumerateControl(cNode.enum)
      
   return cNodeData
}

def addDoxygenFormatFunction(node, index, paramStr, typeStr)
{
    def doxygenStr = ""
   
    doxygenStr += '/**\n' + ' * ' + functionPrefix + node[index].name.text() + '\n'
    doxygenStr +=  ' * @brief ' + node[index].comment.text().replaceAll("\n","\n * ") + '\n'
    
    if(paramStr.size() == 0 && typeStr.size() == 0)
    {
       doxygenStr +=  ' * @return ' +  node[index].return.text() + '\n'
       doxygenStr +=  ' */\n'
    }
    else if(paramStr.size() == typeStr.size())
    {
       index=0
       paramStr.size().times
       {
          doxygenStr +=  ' * @param ' +   paramStr[index] + '\n'
          index++
       }
       doxygenStr +=  ' * @return ' +  node[index].return.text() + '\n'
       doxygenStr +=  ' */\n'
    }
    return doxygenStr
}


def addDoxygenFileFormat()
{
   def cdoxygenStr= ""
   def hdoxygenStr= ""
   
   cdoxygenStr += '/**\n'
   cdoxygenStr +=  ' * @file ' + fileName + '.c' + '\n'
   cdoxygenStr +=  ' * @version ' + version + '\n'
   cdoxygenStr +=  ' * @author ' + author  + '\n'
   cdoxygenStr +=  ' * @copyright ' + copyright  + '\n'
   cdoxygenStr +=  ' * @date ' + date  + '\n'
   cdoxygenStr +=  ' * @brief ' + brief  + '\n'
   cdoxygenStr +=  ' */\n'
   
   hdoxygenStr += '/**\n'
   hdoxygenStr +=  ' * @file ' + fileName + '.h' + '\n'
   hdoxygenStr +=  ' * @version ' + version + '\n'
   hdoxygenStr +=  ' * @author ' + author  + '\n'
   hdoxygenStr +=  ' * @copyright ' + copyright  + '\n'
   hdoxygenStr +=  ' * @date ' + date  + '\n'
   hdoxygenStr +=  ' * @brief ' + brief  + '\n'
   hdoxygenStr +=  ' */\n'
   
   cfileWrite(cdoxygenStr)
   hfileWrite(hdoxygenStr)
}

def addDoxygenIncludesFormat()
{
   def doxygenStr= ""
   
   doxygenStr += '/********************************************************************************************************\n'
   doxygenStr +=  '* Includes\n'
   doxygenStr +=  '********************************************************************************************************/\n'
   
   cfileWrite(doxygenStr)
}

def addDoxygenDefinesFormat()
{
   def doxygenStr= ""
   
   doxygenStr += '/********************************************************************************************************\n'
   doxygenStr +=  '* Defines\n'
   doxygenStr +=  '********************************************************************************************************/\n'
   
   hfileWrite(doxygenStr)
}

def addDoxygenLocalFormat()
{
   def doxygenStr= ""
   
   doxygenStr += '/********************************************************************************************************\n'
   doxygenStr +=  '* Local Variables\n'
   doxygenStr +=  '********************************************************************************************************/\n'
   
    cfileWrite(doxygenStr)
}

def addDoxygenGlobalFormat()
{
   def doxygenStr= ""
   
   doxygenStr += '/********************************************************************************************************\n'
   doxygenStr +=  '* Global Variables\n'
   doxygenStr +=  '********************************************************************************************************/\n'
   
   cfileWrite(doxygenStr)
}

def addDoxygenConstantFormat()
{
   def doxygenStr= ""
   
   doxygenStr += '/********************************************************************************************************\n'
   doxygenStr +=  '* Constant Variables\n'
   doxygenStr +=  '********************************************************************************************************/\n'
   
   cfileWrite(doxygenStr)
}

def addDoxygenTypedefsFormat()
{
   def doxygenStr= ""
   
   doxygenStr += '/********************************************************************************************************\n'
   doxygenStr +=  '* Typedefs\n'
   doxygenStr +=  '********************************************************************************************************/\n'
   
   hfileWrite(doxygenStr)
}

def addDoxygenStaticFunctionFormat()
{
   def doxygenStr= ""
   
   doxygenStr += '/********************************************************************************************************\n'
   doxygenStr +=  '* Static Function Prototypes\n'
   doxygenStr +=  '********************************************************************************************************/\n'
   
   cfileWrite(doxygenStr)
}

def addDoxygenGlobalFunctionFormat()
{
   def doxygenStr= ""
   
   doxygenStr += '/********************************************************************************************************\n'
   doxygenStr +=  '* Global Function Prototypes\n'
   doxygenStr +=  '********************************************************************************************************/\n'
   
   hfileWrite(doxygenStr)
}

def addDoxygenExternGlobalFunctionFormat()
{
   def doxygenStr= ""
   
   doxygenStr += '/********************************************************************************************************\n'
   doxygenStr +=  '* Extern Global Function Prototypes\n'
   doxygenStr +=  '********************************************************************************************************/\n'
   
   hfileWrite(doxygenStr)
}

def addDoxygenFunctionsFormat()
{
   def doxygenStr= ""
   
   doxygenStr += '/********************************************************************************************************\n'
   doxygenStr +=  '* Functions\n'
   doxygenStr +=  '********************************************************************************************************/\n'
   
   cfileWrite(doxygenStr)
}


def addDoxygenExternGlobalVariableFormat()
{
   def doxygenStr= ""
   
   doxygenStr += '/********************************************************************************************************\n'
   doxygenStr +=  '* Extern Global Variables\n'
   doxygenStr +=  '********************************************************************************************************/\n'
   
   hfileWrite(doxygenStr)
}

def addDoxygenEndFormat()
{
    def  doxygenStr= ""
    doxygenStr +=  '/********************************************************************************************************/\n'
    
    return doxygenStr
}

def xmlParser()
{
   def project = new XmlParser().parse("C:/codeGenerate/codeGenerateTemp.xml")
   if( project.name() == 'interface')
   {
      file(project.file)
      cfileInit()
      hfileInit()
      include(project.include)
      define(project.define)
      struct(project.struct)
      enumerate(project.enum)
      union(project.union)
      typedef(project.typedef)
      function(project.function)
      variable(project.variable)
   }
   else
   {
      println  'Project name error'
   }
}

File cfile
def cfileInit()
{
   cfile = new File("C:/codeGenerate/source/" + fileName + ".c")
   cfile.write(" ")
}

File hfile
def hfileInit()
{
   hfile = new File("C:/codeGenerate/source/" + fileName + ".h")
   
   hfile.write("#ifndef " + fileName.toUpperCase() + "\n")
   hfileWrite("#define " + fileName.toUpperCase() + "\n")
}

def cfileWrite(srt)
{
   cfile << srt
}


def hfileWrite(srt)
{
    hfile << srt
}

def codeGenerateProcess()
{
    if(fileName != "")
    {
       addDoxygenFileFormat()
    }
    
    if(includeStr != "")
    {
      addDoxygenIncludesFormat()
      cfileWrite(includeStr + '\n\n')
    }
    
    if(defineStr != "")
    {
      addDoxygenDefinesFormat()
      hfileWrite(defineStr+ '\n\n')
    }
    
    if(typedefStr != "")
    {
      addDoxygenTypedefsFormat()
      hfileWrite(typedefStr+ '\n\n')
    }
    
    if(staticPrototypeFunctionStr != "")
    {
      addDoxygenStaticFunctionFormat()
      cfileWrite(staticPrototypeFunctionStr+ '\n\n')
    }
    
    if(prototypeFunctionStr != "")
    {
      addDoxygenGlobalFunctionFormat()
      hfileWrite(prototypeFunctionStr+ '\n\n')
    }
    
    if(externGlobalVariableStr  != "")
    {
       addDoxygenExternGlobalVariableFormat()
       hfileWrite(externGlobalVariableStr + '\n\n')
    }
    
    if(externPrototypeFunctionStr != "")
    {
      addDoxygenExternGlobalFunctionFormat()
      hfileWrite(externPrototypeFunctionStr + '\n\n')
    }
    
    if( structStr != "" || enumStr != "" || unionStr  != "" || staticVariableStr != "")
    {
       addDoxygenLocalFormat()
       if(enumStr != "")
       {
         cfileWrite(enumStr+ '\n\n')
       }
       if(structStr != "")
       {
         cfileWrite(structStr+ '\n\n')
       }
       if(unionStr != "")
       {
         cfileWrite(unionStr+ '\n\n')
       }
       if(staticVariableStr != "")
       {
         cfileWrite(staticVariableStr+ '\n\n')
       }
    }
    
    if( globalVariableStr != "")
    {
       addDoxygenGlobalFormat()
       cfileWrite(globalVariableStr+ '\n\n')
    }
    
    if( constantVariableStr != "")
    {
       addDoxygenConstantFormat()
       cfileWrite(constantVariableStr+ '\n\n')
    }
    
    if( staticFunctionStr != "" ||  globalFunctionStr != "")
    {
       addDoxygenFunctionsFormat()
       cfileWrite(staticFunctionStr)
       cfileWrite(globalFunctionStr + '\n\n')
    }
    
    cfileWrite(addDoxygenEndFormat())
    hfileWrite(addDoxygenEndFormat())
    hfileWrite("#endif //" + fileName.toUpperCase())
}

xmlParser()
codeGenerateProcess()