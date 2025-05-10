package consultoria.askyu.syntro.interfaces

import org.modelmapper.ModelMapper

abstract class IService(
    val servicoRepository: IRepository,
    val servicoMapper: ModelMapper = ModelMapper()
)