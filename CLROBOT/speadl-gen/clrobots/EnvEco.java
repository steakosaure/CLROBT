package clrobots;

import clrobots.EcoBoite;
import clrobots.EcoNid;
import clrobots.Environnement;
import clrobots.ForwardBoite;
import clrobots.ForwardNid;
import clrobots.interfaces.IBoiteInfo;
import clrobots.interfaces.IEnvInfos;
import clrobots.interfaces.INidInfo;
import clrobots.interfaces.Igui;
import clrobots.interfaces.Iinteragir;

@SuppressWarnings("all")
public abstract class EnvEco {
  public interface Requires {
  }
  
  public interface Component extends EnvEco.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public Igui gui();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public Iinteragir interagir();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public IEnvInfos envInfos();
  }
  
  public interface Parts {
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public ForwardBoite.Component<IBoiteInfo> forwardBoite();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public ForwardNid.Component<INidInfo> forwardNid();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Environnement.Component environnement();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public EcoBoite.Component ecoBoites();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public EcoNid.Component ecoNids();
  }
  
  public static class ComponentImpl implements EnvEco.Component, EnvEco.Parts {
    private final EnvEco.Requires bridge;
    
    private final EnvEco implementation;
    
    public void start() {
      assert this.forwardBoite != null: "This is a bug.";
      ((ForwardBoite.ComponentImpl<IBoiteInfo>) this.forwardBoite).start();
      assert this.forwardNid != null: "This is a bug.";
      ((ForwardNid.ComponentImpl<INidInfo>) this.forwardNid).start();
      assert this.environnement != null: "This is a bug.";
      ((Environnement.ComponentImpl) this.environnement).start();
      assert this.ecoBoites != null: "This is a bug.";
      ((EcoBoite.ComponentImpl) this.ecoBoites).start();
      assert this.ecoNids != null: "This is a bug.";
      ((EcoNid.ComponentImpl) this.ecoNids).start();
      this.implementation.start();
      this.implementation.started = true;
    }
    
    private void init_forwardBoite() {
      assert this.forwardBoite == null: "This is a bug.";
      assert this.implem_forwardBoite == null: "This is a bug.";
      this.implem_forwardBoite = this.implementation.make_forwardBoite();
      if (this.implem_forwardBoite == null) {
      	throw new RuntimeException("make_forwardBoite() in clrobots.EnvEco should not return null.");
      }
      this.forwardBoite = this.implem_forwardBoite._newComponent(new BridgeImpl_forwardBoite(), false);
      
    }
    
    private void init_forwardNid() {
      assert this.forwardNid == null: "This is a bug.";
      assert this.implem_forwardNid == null: "This is a bug.";
      this.implem_forwardNid = this.implementation.make_forwardNid();
      if (this.implem_forwardNid == null) {
      	throw new RuntimeException("make_forwardNid() in clrobots.EnvEco should not return null.");
      }
      this.forwardNid = this.implem_forwardNid._newComponent(new BridgeImpl_forwardNid(), false);
      
    }
    
    private void init_environnement() {
      assert this.environnement == null: "This is a bug.";
      assert this.implem_environnement == null: "This is a bug.";
      this.implem_environnement = this.implementation.make_environnement();
      if (this.implem_environnement == null) {
      	throw new RuntimeException("make_environnement() in clrobots.EnvEco should not return null.");
      }
      this.environnement = this.implem_environnement._newComponent(new BridgeImpl_environnement(), false);
      
    }
    
    private void init_ecoBoites() {
      assert this.ecoBoites == null: "This is a bug.";
      assert this.implem_ecoBoites == null: "This is a bug.";
      this.implem_ecoBoites = this.implementation.make_ecoBoites();
      if (this.implem_ecoBoites == null) {
      	throw new RuntimeException("make_ecoBoites() in clrobots.EnvEco should not return null.");
      }
      this.ecoBoites = this.implem_ecoBoites._newComponent(new BridgeImpl_ecoBoites(), false);
      
    }
    
    private void init_ecoNids() {
      assert this.ecoNids == null: "This is a bug.";
      assert this.implem_ecoNids == null: "This is a bug.";
      this.implem_ecoNids = this.implementation.make_ecoNids();
      if (this.implem_ecoNids == null) {
      	throw new RuntimeException("make_ecoNids() in clrobots.EnvEco should not return null.");
      }
      this.ecoNids = this.implem_ecoNids._newComponent(new BridgeImpl_ecoNids(), false);
      
    }
    
    protected void initParts() {
      init_forwardBoite();
      init_forwardNid();
      init_environnement();
      init_ecoBoites();
      init_ecoNids();
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final EnvEco implem, final EnvEco.Requires b, final boolean doInits) {
      this.bridge = b;
      this.implementation = implem;
      
      assert implem.selfComponent == null: "This is a bug.";
      implem.selfComponent = this;
      
      // prevent them to be called twice if we are in
      // a specialized component: only the last of the
      // hierarchy will call them after everything is initialised
      if (doInits) {
      	initParts();
      	initProvidedPorts();
      }
    }
    
    public Igui gui() {
      return this.environnement().gui();
    }
    
    public Iinteragir interagir() {
      return this.environnement().interagir();
    }
    
    public IEnvInfos envInfos() {
      return this.environnement().envInfos();
    }
    
    private ForwardBoite.Component<IBoiteInfo> forwardBoite;
    
    private ForwardBoite<IBoiteInfo> implem_forwardBoite;
    
    private final class BridgeImpl_forwardBoite implements ForwardBoite.Requires<IBoiteInfo> {
    }
    
    public final ForwardBoite.Component<IBoiteInfo> forwardBoite() {
      return this.forwardBoite;
    }
    
    private ForwardNid.Component<INidInfo> forwardNid;
    
    private ForwardNid<INidInfo> implem_forwardNid;
    
    private final class BridgeImpl_forwardNid implements ForwardNid.Requires<INidInfo> {
    }
    
    public final ForwardNid.Component<INidInfo> forwardNid() {
      return this.forwardNid;
    }
    
    private Environnement.Component environnement;
    
    private Environnement implem_environnement;
    
    private final class BridgeImpl_environnement implements Environnement.Requires {
      public final IBoiteInfo boitesInfos() {
        return EnvEco.ComponentImpl.this.forwardBoite().i();
      }
      
      public final INidInfo nidInfos() {
        return EnvEco.ComponentImpl.this.forwardNid().i();
      }
    }
    
    public final Environnement.Component environnement() {
      return this.environnement;
    }
    
    private EcoBoite.Component ecoBoites;
    
    private EcoBoite implem_ecoBoites;
    
    private final class BridgeImpl_ecoBoites implements EcoBoite.Requires {
    }
    
    public final EcoBoite.Component ecoBoites() {
      return this.ecoBoites;
    }
    
    private EcoNid.Component ecoNids;
    
    private EcoNid implem_ecoNids;
    
    private final class BridgeImpl_ecoNids implements EcoNid.Requires {
    }
    
    public final EcoNid.Component ecoNids() {
      return this.ecoNids;
    }
  }
  
  public static class DynamicAssembledBoite {
    public interface Requires {
    }
    
    public interface Component extends EnvEco.DynamicAssembledBoite.Provides {
    }
    
    public interface Provides {
    }
    
    public interface Parts {
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public EcoBoite.Boite.Component eb();
      
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public ForwardBoite.AgentForward.Component<IBoiteInfo> fb();
    }
    
    public static class ComponentImpl implements EnvEco.DynamicAssembledBoite.Component, EnvEco.DynamicAssembledBoite.Parts {
      private final EnvEco.DynamicAssembledBoite.Requires bridge;
      
      private final EnvEco.DynamicAssembledBoite implementation;
      
      public void start() {
        assert this.eb != null: "This is a bug.";
        ((EcoBoite.Boite.ComponentImpl) this.eb).start();
        assert this.fb != null: "This is a bug.";
        ((ForwardBoite.AgentForward.ComponentImpl<IBoiteInfo>) this.fb).start();
        this.implementation.start();
        this.implementation.started = true;
      }
      
      private void init_eb() {
        assert this.eb == null: "This is a bug.";
        assert this.implementation.use_eb != null: "This is a bug.";
        this.eb = this.implementation.use_eb._newComponent(new BridgeImpl_ecoBoites_eb(), false);
        
      }
      
      private void init_fb() {
        assert this.fb == null: "This is a bug.";
        assert this.implementation.use_fb != null: "This is a bug.";
        this.fb = this.implementation.use_fb._newComponent(new BridgeImpl_forwardBoite_fb(), false);
        
      }
      
      protected void initParts() {
        init_eb();
        init_fb();
      }
      
      protected void initProvidedPorts() {
        
      }
      
      public ComponentImpl(final EnvEco.DynamicAssembledBoite implem, final EnvEco.DynamicAssembledBoite.Requires b, final boolean doInits) {
        this.bridge = b;
        this.implementation = implem;
        
        assert implem.selfComponent == null: "This is a bug.";
        implem.selfComponent = this;
        
        // prevent them to be called twice if we are in
        // a specialized component: only the last of the
        // hierarchy will call them after everything is initialised
        if (doInits) {
        	initParts();
        	initProvidedPorts();
        }
      }
      
      private EcoBoite.Boite.Component eb;
      
      private final class BridgeImpl_ecoBoites_eb implements EcoBoite.Boite.Requires {
      }
      
      public final EcoBoite.Boite.Component eb() {
        return this.eb;
      }
      
      private ForwardBoite.AgentForward.Component<IBoiteInfo> fb;
      
      private final class BridgeImpl_forwardBoite_fb implements ForwardBoite.AgentForward.Requires<IBoiteInfo> {
        public final IBoiteInfo a() {
          return EnvEco.DynamicAssembledBoite.ComponentImpl.this.eb().boiteinfo();
        }
      }
      
      public final ForwardBoite.AgentForward.Component<IBoiteInfo> fb() {
        return this.fb;
      }
    }
    
    /**
     * Used to check that two components are not created from the same implementation,
     * that the component has been started to call requires(), provides() and parts()
     * and that the component is not started by hand.
     * 
     */
    private boolean init = false;;
    
    /**
     * Used to check that the component is not started by hand.
     * 
     */
    private boolean started = false;;
    
    private EnvEco.DynamicAssembledBoite.ComponentImpl selfComponent;
    
    /**
     * Can be overridden by the implementation.
     * It will be called automatically after the component has been instantiated.
     * 
     */
    protected void start() {
      if (!this.init || this.started) {
      	throw new RuntimeException("start() should not be called by hand: to create a new component, use newComponent().");
      }
    }
    
    /**
     * This can be called by the implementation to access the provided ports.
     * 
     */
    protected EnvEco.DynamicAssembledBoite.Provides provides() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    /**
     * This can be called by the implementation to access the required ports.
     * 
     */
    protected EnvEco.DynamicAssembledBoite.Requires requires() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("requires() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if requires() is needed to initialise the component.");
      }
      return this.selfComponent.bridge;
    }
    
    /**
     * This can be called by the implementation to access the parts and their provided ports.
     * 
     */
    protected EnvEco.DynamicAssembledBoite.Parts parts() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    private EcoBoite.Boite use_eb;
    
    private ForwardBoite.AgentForward<IBoiteInfo> use_fb;
    
    /**
     * Not meant to be used to manually instantiate components (except for testing).
     * 
     */
    public synchronized EnvEco.DynamicAssembledBoite.Component _newComponent(final EnvEco.DynamicAssembledBoite.Requires b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of DynamicAssembledBoite has already been used to create a component, use another one.");
      }
      this.init = true;
      EnvEco.DynamicAssembledBoite.ComponentImpl  _comp = new EnvEco.DynamicAssembledBoite.ComponentImpl(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private EnvEco.ComponentImpl ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected EnvEco.Provides eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected EnvEco.Requires eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected EnvEco.Parts eco_parts() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
  }
  
  public static class DynamicAssembledNid {
    public interface Requires {
    }
    
    public interface Component extends EnvEco.DynamicAssembledNid.Provides {
    }
    
    public interface Provides {
    }
    
    public interface Parts {
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public EcoNid.Nid.Component en();
      
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public ForwardNid.AgentForward.Component<INidInfo> fn();
    }
    
    public static class ComponentImpl implements EnvEco.DynamicAssembledNid.Component, EnvEco.DynamicAssembledNid.Parts {
      private final EnvEco.DynamicAssembledNid.Requires bridge;
      
      private final EnvEco.DynamicAssembledNid implementation;
      
      public void start() {
        assert this.en != null: "This is a bug.";
        ((EcoNid.Nid.ComponentImpl) this.en).start();
        assert this.fn != null: "This is a bug.";
        ((ForwardNid.AgentForward.ComponentImpl<INidInfo>) this.fn).start();
        this.implementation.start();
        this.implementation.started = true;
      }
      
      private void init_en() {
        assert this.en == null: "This is a bug.";
        assert this.implementation.use_en != null: "This is a bug.";
        this.en = this.implementation.use_en._newComponent(new BridgeImpl_ecoNids_en(), false);
        
      }
      
      private void init_fn() {
        assert this.fn == null: "This is a bug.";
        assert this.implementation.use_fn != null: "This is a bug.";
        this.fn = this.implementation.use_fn._newComponent(new BridgeImpl_forwardNid_fn(), false);
        
      }
      
      protected void initParts() {
        init_en();
        init_fn();
      }
      
      protected void initProvidedPorts() {
        
      }
      
      public ComponentImpl(final EnvEco.DynamicAssembledNid implem, final EnvEco.DynamicAssembledNid.Requires b, final boolean doInits) {
        this.bridge = b;
        this.implementation = implem;
        
        assert implem.selfComponent == null: "This is a bug.";
        implem.selfComponent = this;
        
        // prevent them to be called twice if we are in
        // a specialized component: only the last of the
        // hierarchy will call them after everything is initialised
        if (doInits) {
        	initParts();
        	initProvidedPorts();
        }
      }
      
      private EcoNid.Nid.Component en;
      
      private final class BridgeImpl_ecoNids_en implements EcoNid.Nid.Requires {
      }
      
      public final EcoNid.Nid.Component en() {
        return this.en;
      }
      
      private ForwardNid.AgentForward.Component<INidInfo> fn;
      
      private final class BridgeImpl_forwardNid_fn implements ForwardNid.AgentForward.Requires<INidInfo> {
        public final INidInfo a() {
          return EnvEco.DynamicAssembledNid.ComponentImpl.this.en().nidinfo();
        }
      }
      
      public final ForwardNid.AgentForward.Component<INidInfo> fn() {
        return this.fn;
      }
    }
    
    /**
     * Used to check that two components are not created from the same implementation,
     * that the component has been started to call requires(), provides() and parts()
     * and that the component is not started by hand.
     * 
     */
    private boolean init = false;;
    
    /**
     * Used to check that the component is not started by hand.
     * 
     */
    private boolean started = false;;
    
    private EnvEco.DynamicAssembledNid.ComponentImpl selfComponent;
    
    /**
     * Can be overridden by the implementation.
     * It will be called automatically after the component has been instantiated.
     * 
     */
    protected void start() {
      if (!this.init || this.started) {
      	throw new RuntimeException("start() should not be called by hand: to create a new component, use newComponent().");
      }
    }
    
    /**
     * This can be called by the implementation to access the provided ports.
     * 
     */
    protected EnvEco.DynamicAssembledNid.Provides provides() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    /**
     * This can be called by the implementation to access the required ports.
     * 
     */
    protected EnvEco.DynamicAssembledNid.Requires requires() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("requires() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if requires() is needed to initialise the component.");
      }
      return this.selfComponent.bridge;
    }
    
    /**
     * This can be called by the implementation to access the parts and their provided ports.
     * 
     */
    protected EnvEco.DynamicAssembledNid.Parts parts() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    private EcoNid.Nid use_en;
    
    private ForwardNid.AgentForward<INidInfo> use_fn;
    
    /**
     * Not meant to be used to manually instantiate components (except for testing).
     * 
     */
    public synchronized EnvEco.DynamicAssembledNid.Component _newComponent(final EnvEco.DynamicAssembledNid.Requires b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of DynamicAssembledNid has already been used to create a component, use another one.");
      }
      this.init = true;
      EnvEco.DynamicAssembledNid.ComponentImpl  _comp = new EnvEco.DynamicAssembledNid.ComponentImpl(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private EnvEco.ComponentImpl ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected EnvEco.Provides eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected EnvEco.Requires eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected EnvEco.Parts eco_parts() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
  }
  
  /**
   * Used to check that two components are not created from the same implementation,
   * that the component has been started to call requires(), provides() and parts()
   * and that the component is not started by hand.
   * 
   */
  private boolean init = false;;
  
  /**
   * Used to check that the component is not started by hand.
   * 
   */
  private boolean started = false;;
  
  private EnvEco.ComponentImpl selfComponent;
  
  /**
   * Can be overridden by the implementation.
   * It will be called automatically after the component has been instantiated.
   * 
   */
  protected void start() {
    if (!this.init || this.started) {
    	throw new RuntimeException("start() should not be called by hand: to create a new component, use newComponent().");
    }
  }
  
  /**
   * This can be called by the implementation to access the provided ports.
   * 
   */
  protected EnvEco.Provides provides() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected EnvEco.Requires requires() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("requires() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if requires() is needed to initialise the component.");
    }
    return this.selfComponent.bridge;
  }
  
  /**
   * This can be called by the implementation to access the parts and their provided ports.
   * 
   */
  protected EnvEco.Parts parts() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract ForwardBoite<IBoiteInfo> make_forwardBoite();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract ForwardNid<INidInfo> make_forwardNid();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Environnement make_environnement();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract EcoBoite make_ecoBoites();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract EcoNid make_ecoNids();
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized EnvEco.Component _newComponent(final EnvEco.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of EnvEco has already been used to create a component, use another one.");
    }
    this.init = true;
    EnvEco.ComponentImpl  _comp = new EnvEco.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected EnvEco.DynamicAssembledBoite make_DynamicAssembledBoite() {
    return new EnvEco.DynamicAssembledBoite();
  }
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public EnvEco.DynamicAssembledBoite _createImplementationOfDynamicAssembledBoite() {
    EnvEco.DynamicAssembledBoite implem = make_DynamicAssembledBoite();
    if (implem == null) {
    	throw new RuntimeException("make_DynamicAssembledBoite() in clrobots.EnvEco should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    assert this.selfComponent.implem_ecoBoites != null: "This is a bug.";
    assert implem.use_eb == null: "This is a bug.";
    implem.use_eb = this.selfComponent.implem_ecoBoites._createImplementationOfBoite();
    assert this.selfComponent.implem_forwardBoite != null: "This is a bug.";
    assert implem.use_fb == null: "This is a bug.";
    implem.use_fb = this.selfComponent.implem_forwardBoite._createImplementationOfAgentForward();
    return implem;
  }
  
  /**
   * This can be called to create an instance of the species from inside the implementation of the ecosystem.
   * 
   */
  protected EnvEco.DynamicAssembledBoite.Component newDynamicAssembledBoite() {
    EnvEco.DynamicAssembledBoite _implem = _createImplementationOfDynamicAssembledBoite();
    return _implem._newComponent(new EnvEco.DynamicAssembledBoite.Requires() {},true);
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected EnvEco.DynamicAssembledNid make_DynamicAssembledNid() {
    return new EnvEco.DynamicAssembledNid();
  }
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public EnvEco.DynamicAssembledNid _createImplementationOfDynamicAssembledNid() {
    EnvEco.DynamicAssembledNid implem = make_DynamicAssembledNid();
    if (implem == null) {
    	throw new RuntimeException("make_DynamicAssembledNid() in clrobots.EnvEco should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    assert this.selfComponent.implem_ecoNids != null: "This is a bug.";
    assert implem.use_en == null: "This is a bug.";
    implem.use_en = this.selfComponent.implem_ecoNids._createImplementationOfNid();
    assert this.selfComponent.implem_forwardNid != null: "This is a bug.";
    assert implem.use_fn == null: "This is a bug.";
    implem.use_fn = this.selfComponent.implem_forwardNid._createImplementationOfAgentForward();
    return implem;
  }
  
  /**
   * This can be called to create an instance of the species from inside the implementation of the ecosystem.
   * 
   */
  protected EnvEco.DynamicAssembledNid.Component newDynamicAssembledNid() {
    EnvEco.DynamicAssembledNid _implem = _createImplementationOfDynamicAssembledNid();
    return _implem._newComponent(new EnvEco.DynamicAssembledNid.Requires() {},true);
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public EnvEco.Component newComponent() {
    return this._newComponent(new EnvEco.Requires() {}, true);
  }
}
