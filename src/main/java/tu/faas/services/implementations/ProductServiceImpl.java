package tu.faas.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tu.faas.domain.entities.Product;
import tu.faas.domain.models.view.ProductHomeViewModel;
import tu.faas.repositories.ProductRepository;
import tu.faas.services.contracts.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductHomeViewModel> getNewestProductHomeViewModels(Integer count) {
        List<Product> products = productRepository.findAllByOrderByIdDesc(new PageRequest(0, count));
        return products
                .stream()
                .map(product -> {
                    ProductHomeViewModel result = modelMapper.map(product, ProductHomeViewModel.class);
                    result.setRestaurantName(product.getRestaurant().getName());
                    return result;
                }).collect(Collectors.toList());
    }
}
