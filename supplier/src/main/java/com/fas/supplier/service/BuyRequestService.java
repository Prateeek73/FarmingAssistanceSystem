package com.fas.supplier.service;

import com.fas.supplier.SupplierApplication;
import com.fas.supplier.constants.RequestStatus;
import com.fas.supplier.dtos.AddBuyRequest;
import com.fas.supplier.dtos.BuyRequestDetails;
import com.fas.supplier.dtos.UserDetails;
import com.fas.supplier.entities.BuyRequest;
import com.fas.supplier.entities.Supplier;
import com.fas.supplier.exceptions.BuyRequestIdNotFoundException;
import com.fas.supplier.exceptions.FarmerNotFoundException;
import com.fas.supplier.exceptions.ProductNotFoundException;
import com.fas.supplier.exceptions.SupplierNotFoundException;
import com.fas.supplier.repository.IBuyRequestRepository;
import com.fas.supplier.repository.ISupplierRepository;
import com.fas.supplier.utilities.BuyRequestUtility;
import com.fas.supplier.utilities.SupplierUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BuyRequestService implements IBuyRequestService {


    Logger logger = LoggerFactory.getLogger(SupplierApplication.class);

    @Autowired
    private BuyRequestUtility buyRequestUtility;
    @Autowired
    private SupplierUtility suppliersUtil;
    @Autowired
    private IBuyRequestRepository buyRequestRepository;
    @Autowired
    private ISupplierRepository suppliersRepository;

    @Override
    public BuyRequestDetails sendBuyRequest(AddBuyRequest addBuyRequest) {

        Supplier supplier = getSupplierFromId(addBuyRequest.getSupplierId());
        UserDetails userDetails = suppliersUtil.getUserDetails(supplier.getUsername());
        suppliersUtil.isSupplierLoggedIn(userDetails);

        BuyRequest buyRequest = new BuyRequest();
        buyRequestUtility.getProductDetailsById(addBuyRequest.getProductId());

        buyRequest.setProductId(addBuyRequest.getProductId());
        buyRequest.setSupplierId(addBuyRequest.getSupplierId());
        buyRequest.setAskedPrice(addBuyRequest.getAskedPrice());
        buyRequest.setRequestStatus(RequestStatus.PENDING);
        buyRequestRepository.save(buyRequest);


        return buyRequestUtility.toBuyRequestDetails(buyRequest, supplier);
    }

    @Override
    public BuyRequestDetails getBuyRequestsById(Long buyRequestId) {
        BuyRequest buyRequest = getBuyRequestFromId(buyRequestId);
        Supplier supplier = getSupplierFromId(buyRequest.getSupplierId());
        UserDetails userDetails = suppliersUtil.getUserDetails(supplier.getUsername());
        suppliersUtil.isSupplierLoggedIn(userDetails);

        return buyRequestUtility.toBuyRequestDetails(buyRequest, supplier);
    }

    @Override
    public List<BuyRequestDetails> getBuyRequestByProductId(Long productId) {
        List<BuyRequest> buyRequestList = buyRequestRepository.getBuyRequestByProductId(productId);
        if(buyRequestList.isEmpty())
            throw new ProductNotFoundException("Product not found for id: "+productId);

        List<BuyRequestDetails> buyRequestDetailsList = new ArrayList<>();
        for (BuyRequest buyRequest : buyRequestList) {
            Supplier supplier = getSupplierFromId(buyRequest.getSupplierId());
            BuyRequestDetails buyRequestDetails = buyRequestUtility.toBuyRequestDetails(buyRequest, supplier);
            buyRequestDetailsList.add(buyRequestDetails);
        }
        return buyRequestDetailsList;
    }

    @Override
    public List<BuyRequestDetails> getBuyRequestByFarmerId(Long farmerId) {
        List<BuyRequest> buyRequestList = buyRequestRepository.getBuyRequestByFarmerId(farmerId);
        if(buyRequestList.isEmpty())
            throw new FarmerNotFoundException("No farmer found for id: " + farmerId);

        List<BuyRequestDetails> buyRequestDetailsList = new ArrayList<>();
        for (BuyRequest buyRequest : buyRequestList) {
            Supplier supplier = getSupplierFromId(buyRequest.getSupplierId());
            BuyRequestDetails buyRequestDetails = buyRequestUtility.toBuyRequestDetails(buyRequest, supplier);
            buyRequestDetailsList.add(buyRequestDetails);
        }
        return buyRequestDetailsList;
    }

    @Override
    public BuyRequestDetails approveBuyRequest(Long buyRequestId) {
        BuyRequest buyRequest = getBuyRequestFromId(buyRequestId);

        List<BuyRequest> buyRequestList = buyRequestRepository.getBuyRequestByProductId(buyRequest.getProductId());
        for(BuyRequest buyRequestIter : buyRequestList) {
            buyRequestIter.setRequestStatus(RequestStatus.REJECTED);
            buyRequestRepository.save(buyRequestIter);
        }

        buyRequest.setRequestStatus(RequestStatus.APPROVED);

        buyRequestRepository.save(buyRequest);
        Supplier supplier = getSupplierFromId(buyRequest.getSupplierId());
        return buyRequestUtility.toBuyRequestDetails(buyRequest, supplier);

    }

    @Override
    public BuyRequestDetails rejectBuyRequest(Long buyRequestId) {
        BuyRequest buyRequest = getBuyRequestFromId(buyRequestId);
        buyRequest.setRequestStatus(RequestStatus.REJECTED);
        buyRequestRepository.save(buyRequest);
        Supplier supplier = getSupplierFromId(buyRequest.getSupplierId());
        return buyRequestUtility.toBuyRequestDetails(buyRequest, supplier);
    }

    @Override
    public List<BuyRequestDetails> getBuyRequestBySupplier(Long supplierId) {
        List<BuyRequest> buyRequestList = buyRequestRepository.getBuyRequestBySupplierId(supplierId);
        if(buyRequestList.isEmpty())
            throw new SupplierNotFoundException("No supplier found for id: "+supplierId);

        Supplier supplier = getSupplierFromId(supplierId);
        UserDetails userDetails = suppliersUtil.getUserDetails(supplier.getUsername());
        suppliersUtil.isSupplierLoggedIn(userDetails);


        List<BuyRequestDetails> buyRequestDetailsList = new ArrayList<>();
        for (BuyRequest buyRequest : buyRequestList) {
            Supplier supplierIter = getSupplierFromId(buyRequest.getSupplierId());
            BuyRequestDetails buyRequestDetails = buyRequestUtility.toBuyRequestDetails(buyRequest, supplierIter);
            buyRequestDetailsList.add(buyRequestDetails);
        }
        return buyRequestDetailsList;
    }

    public Supplier getSupplierFromId(Long supplierId){
        Optional<Supplier> supplierOptional = suppliersRepository.findById(supplierId);
        if (!supplierOptional.isPresent())
            throw new SupplierNotFoundException("No supplier found for id: "+supplierId);
        return supplierOptional.get();
    }

    public BuyRequest getBuyRequestFromId(Long buyRequestId){
        Optional<BuyRequest> buyRequestOptional = buyRequestRepository.findById(buyRequestId);
        if(!buyRequestOptional.isPresent())
            throw new BuyRequestIdNotFoundException("No Buy Requests found for id: "+buyRequestId);
        return buyRequestOptional.get();
    }
}
