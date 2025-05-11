package consultoria.askyu.syntro.interfaces

import org.modelmapper.ModelMapper

abstract class IService(
    val serviceRepository: IRepository,
    val mapper: ModelMapper = ModelMapper()
)