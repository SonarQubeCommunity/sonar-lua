 local _init = function(bsz, nhid, t, cache)
    local t = t or 'torch.Tensor'
    local tensor = cache or torch.Tensor()
    return tensor:resize(bsz, nhid):type(t):fill(0)
end
